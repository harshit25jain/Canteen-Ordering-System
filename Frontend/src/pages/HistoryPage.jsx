import React, { useState, useEffect } from 'react'
import { Card } from '../components/ui/Card'
import { Badge } from '../components/ui/Badge'
import { Button } from '../components/ui/Button'
import { Select } from '../components/ui/Select'
import { CheckCircle, XCircle, Clock, Filter } from 'lucide-react'
import { orderService } from '../../lib/services/order-service'

export default function HistoryPage() {
  const [orders, setOrders] = useState([])
  const [filteredOrders, setFilteredOrders] = useState([])
  const [statusFilter, setStatusFilter] = useState('ALL')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchOrderHistory()
  }, [])

  useEffect(() => {
    filterOrders()
  }, [orders, statusFilter])

  const fetchOrderHistory = async () => {
    try {
      const orderHistory = await orderService.getOrderHistory()
      setOrders(orderHistory)
    } catch (error) {
      console.error('Failed to load order history:', error)
    } finally {
      setLoading(false)
    }
  }

  const filterOrders = () => {
    if (statusFilter === 'ALL') {
      setFilteredOrders(orders)
    } else {
      const filtered = orders.filter(order => order.status === statusFilter)
      setFilteredOrders(filtered)
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
          {[...Array(5)].map((_, i) => (
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

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-8">
        <h1 className="text-4xl font-bold mb-4">Order History</h1>
        <p className="text-muted-foreground mb-6">
          View all your past orders and their status
        </p>
        
        <div className="flex items-center gap-4">
          <Filter className="h-4 w-4" />
          <Select
            value={statusFilter}
            onValueChange={setStatusFilter}
          >
            <option value="ALL">All Orders</option>
            <option value="PENDING">Pending</option>
            <option value="PAID">Paid</option>
            <option value="CANCELLED">Cancelled</option>
          </Select>
        </div>
      </div>

      {filteredOrders.length === 0 ? (
        <div className="text-center py-12">
          <Clock className="mx-auto h-24 w-24 text-muted-foreground mb-4" />
          <h2 className="text-2xl font-bold mb-2">No orders found</h2>
          <p className="text-muted-foreground">
            {statusFilter === 'ALL' 
              ? "You haven't placed any orders yet"
              : `No ${statusFilter.toLowerCase()} orders found`
            }
          </p>
        </div>
      ) : (
        <div className="space-y-4">
          {filteredOrders.map((order) => (
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
                  <p className="text-sm text-muted-foreground">
                    {new Date(order.createdAt).toLocaleDateString()}
                  </p>
                </div>
              </div>

              <div className="mb-4">
                <h4 className="font-medium text-lg">{order.menuItem.name}</h4>
                <p className="text-muted-foreground">
                  {new Date(order.createdAt).toLocaleString()}
                </p>
              </div>

              <div className="flex items-center justify-between text-sm text-muted-foreground">
                <span>Order ID: #{order.id}</span>
                <span>Updated: {new Date(order.updatedAt).toLocaleString()}</span>
              </div>
            </Card>
          ))}
        </div>
      )}
    </div>
  )
}




