import React, { useState, useEffect } from 'react'
import { Card } from '../components/ui/Card'
import { Button } from '../components/ui/Button'
import { Badge } from '../components/ui/Badge'
import { Progress } from '../components/ui/Progress'
import { Clock, CheckCircle, XCircle, CreditCard } from 'lucide-react'
import { orderService } from '../../lib/services/order-service'
import { toast } from 'sonner'

export default function OrdersPage() {
  const [orders, setOrders] = useState([])
  const [loading, setLoading] = useState(true)
  const [now, setNow] = useState(Date.now())

  useEffect(() => {
    fetchOrders()
  }, [])

  // Tick every second to drive live countdowns
  useEffect(() => {
    const id = setInterval(() => setNow(Date.now()), 1000)
    return () => clearInterval(id)
  }, [])

  const fetchOrders = async () => {
    try {
      const pendingOrders = await orderService.getPendingOrders()
      setOrders(pendingOrders)
    } catch (error) {
      toast.error('Failed to load orders')
    } finally {
      setLoading(false)
    }
  }

  const handlePayOrder = async (orderId) => {
    try {
      await orderService.payOrder(orderId)
      toast.success('Order paid successfully!')
      fetchOrders() // Refresh the list
    } catch (error) {
      toast.error('Failed to pay order')
    }
  }

  const handleCancelOrder = async (orderId) => {
    try {
      await orderService.cancelOrder(orderId)
      toast.success('Order cancelled!')
      fetchOrders() // Refresh the list
    } catch (error) {
      toast.error('Failed to cancel order')
    }
  }

  const getStatusIcon = (status) => {
    switch (status) {
      case 'PENDING':
        return <Clock className="h-4 w-4" />
      case 'PAID':
        return <CheckCircle className="h-4 w-4" />
      case 'CANCELLED':
        return <XCircle className="h-4 w-4" />
      default:
        return <Clock className="h-4 w-4" />
    }
  }

  const getStatusColor = (status) => {
    switch (status) {
      case 'PENDING':
        return 'bg-yellow-100 text-yellow-800'
      case 'PAID':
        return 'bg-green-100 text-green-800'
      case 'CANCELLED':
        return 'bg-red-100 text-red-800'
      default:
        return 'bg-gray-100 text-gray-800'
    }
  }

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="space-y-4">
          {[...Array(3)].map((_, i) => (
            <Card key={i} className="p-6 animate-pulse">
              <div className="h-4 bg-muted rounded mb-4"></div>
              <div className="h-6 bg-muted rounded mb-2"></div>
              <div className="h-4 bg-muted rounded w-1/2"></div>
            </Card>
          ))}
        </div>
      </div>
    )
  }

  // Constants for countdown math
  const COUNTDOWN_MINUTES = 15
  const COUNTDOWN_MS = COUNTDOWN_MINUTES * 60 * 1000

  const getRemainingMs = (createdAtIso) => {
    const createdMs = new Date(createdAtIso).getTime()
    const deadline = createdMs + COUNTDOWN_MS
    return Math.max(0, deadline - now)
  }

  const formatRemaining = (ms) => {
    const totalSeconds = Math.ceil(ms / 1000)
    const minutes = Math.floor(totalSeconds / 60)
    const seconds = totalSeconds % 60
    const padded = seconds < 10 ? `0${seconds}` : seconds
    return `${minutes}:${padded}`
  }

  const getProgressPercent = (msRemaining) => {
    const elapsed = COUNTDOWN_MS - msRemaining
    const ratio = Math.min(1, Math.max(0, elapsed / COUNTDOWN_MS))
    return Math.round(ratio * 100)
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-8">
        <h1 className="text-4xl font-bold mb-4">Your Orders</h1>
        <p className="text-muted-foreground">
          Track your pending orders and their status
        </p>
      </div>

      {orders.length === 0 ? (
        <div className="text-center py-12">
          <Clock className="mx-auto h-24 w-24 text-muted-foreground mb-4" />
          <h2 className="text-2xl font-bold mb-2">No pending orders</h2>
          <p className="text-muted-foreground">
            You don't have any pending orders at the moment
          </p>
        </div>
      ) : (
        <div className="space-y-6">
          {orders.map((order) => (
            <Card key={order.id} className="p-6">
              <div className="flex items-center justify-between mb-4">
                <div className="flex items-center gap-3">
                  {getStatusIcon(order.status)}
                  <h3 className="text-xl font-semibold">Order #{order.id}</h3>
                  <Badge className={getStatusColor(order.status)}>
                    {order.status}
                  </Badge>
                </div>
                <div className="text-right">
                  <p className="text-2xl font-bold text-primary">
                    ${order.menuItem.price.toFixed(2)}
                  </p>
                </div>
              </div>

              <div className="mb-4">
                <h4 className="font-medium text-lg">{order.menuItem.name}</h4>
                <p className="text-muted-foreground">
                  Ordered at {new Date(order.createdAt).toLocaleString()}
                </p>
              </div>

              {order.status === 'PENDING' && (() => {
                const remainingMs = getRemainingMs(order.createdAt)
                const expired = remainingMs === 0
                const percent = getProgressPercent(remainingMs)
                return (
                  <div className="mb-4">
                    <div className="flex items-center justify-between mb-2">
                      <span className="text-sm font-medium">Time remaining</span>
                      <span className="text-sm text-muted-foreground">{formatRemaining(remainingMs)}</span>
                    </div>
                    <Progress value={percent} className="h-2" />
                    {expired && (
                      <p className="mt-2 text-xs text-muted-foreground">Order will auto-cancel shortly.</p>
                    )}
                  </div>
                )
              })()}

              <div className="flex gap-3">
                {order.status === 'PENDING' && (() => {
                  const remainingMs = getRemainingMs(order.createdAt)
                  const expired = remainingMs === 0
                  return (
                  <>
                    <Button
                      onClick={() => handlePayOrder(order.id)}
                      disabled={expired}
                      className="flex-1"
                    >
                      <CreditCard className="mr-2 h-4 w-4" />
                      Pay Now
                    </Button>
                    <Button
                      variant="outline"
                      onClick={() => handleCancelOrder(order.id)}
                      disabled={expired}
                    >
                      Cancel Order
                    </Button>
                  </>
                  )
                })()}
              </div>
            </Card>
          ))}
        </div>
      )}
    </div>
  )
}


