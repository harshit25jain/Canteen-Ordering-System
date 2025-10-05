import React, { useState } from 'react'
import { Link } from 'react-router-dom'
import { Button } from '../components/ui/Button'
import { Card } from '../components/ui/Card'
import { Badge } from '../components/ui/Badge'
import { Trash2, ShoppingBag, ArrowLeft } from 'lucide-react'
import { useCart } from '../../lib/hooks/use-cart'
import { orderService } from '../../lib/services/order-service'
import { toast } from 'sonner'

export default function CartPage() {
  const { items, removeItem, updateQuantity, clearCart, getTotalPrice, getTotalItems } = useCart()
  const [isProcessing, setIsProcessing] = useState(false)

  const handleQuantityChange = (menuItemId, newQuantity) => {
    if (newQuantity <= 0) {
      removeItem(menuItemId)
    } else {
      updateQuantity(menuItemId, newQuantity)
    }
  }

  const handleCheckout = async () => {
    if (items.length === 0) {
      toast.error('Your cart is empty')
      return
    }

    setIsProcessing(true)
    try {
      // For simplicity, we'll create one order per item
      // In a real app, you might want to batch items or create a single order
      for (const item of items) {
        for (let i = 0; i < item.quantity; i++) {
          await orderService.createOrder(item.menuItemId)
        }
      }
      
      clearCart()
      toast.success('Orders created successfully!')
    } catch (error) {
      toast.error('Failed to create orders. Please try again.')
    } finally {
      setIsProcessing(false)
    }
  }

  if (items.length === 0) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="text-center py-12">
          <ShoppingBag className="mx-auto h-24 w-24 text-muted-foreground mb-4" />
          <h2 className="text-2xl font-bold mb-2">Your cart is empty</h2>
          <p className="text-muted-foreground mb-6">
            Add some delicious items to get started
          </p>
          <Button asChild>
            <Link to="/menu">
              <ArrowLeft className="mr-2 h-4 w-4" />
              Browse Menu
            </Link>
          </Button>
        </div>
      </div>
    )
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-8">
        <h1 className="text-4xl font-bold mb-4">Shopping Cart</h1>
        <p className="text-muted-foreground">
          {getTotalItems()} {getTotalItems() === 1 ? 'item' : 'items'} in your cart
        </p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2 space-y-4">
          {items.map((item) => (
            <Card key={item.menuItemId} className="p-6">
              <div className="flex items-center justify-between">
                <div className="flex-1">
                  <h3 className="text-xl font-semibold mb-2">{item.name}</h3>
                  <p className="text-2xl font-bold text-primary">
                    ${item.price.toFixed(2)}
                  </p>
                  <Badge variant="secondary" className="mt-2">
                    {item.stockCount} in stock
                  </Badge>
                </div>

                <div className="flex items-center gap-4">
                  <div className="flex items-center gap-2">
                    <Button
                      size="sm"
                      variant="outline"
                      onClick={() => handleQuantityChange(item.menuItemId, item.quantity - 1)}
                    >
                      -
                    </Button>
                    <span className="w-8 text-center font-medium">{item.quantity}</span>
                    <Button
                      size="sm"
                      variant="outline"
                      onClick={() => handleQuantityChange(item.menuItemId, item.quantity + 1)}
                      disabled={item.quantity >= item.stockCount}
                    >
                      +
                    </Button>
                  </div>

                  <Button
                    size="sm"
                    variant="destructive"
                    onClick={() => removeItem(item.menuItemId)}
                  >
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
              </div>
            </Card>
          ))}
        </div>

        <div className="lg:col-span-1">
          <Card className="p-6 sticky top-4">
            <h3 className="text-xl font-semibold mb-4">Order Summary</h3>
            
            <div className="space-y-4">
              <div className="flex justify-between">
                <span>Subtotal ({getTotalItems()} items)</span>
                <span className="font-semibold">${getTotalPrice().toFixed(2)}</span>
              </div>
              
              <div className="flex justify-between text-lg font-bold">
                <span>Total</span>
                <span>${getTotalPrice().toFixed(2)}</span>
              </div>

              <Button
                onClick={handleCheckout}
                disabled={isProcessing}
                className="w-full"
                size="lg"
              >
                {isProcessing ? 'Processing...' : 'Place Orders'}
              </Button>

              <Button
                variant="outline"
                onClick={clearCart}
                className="w-full"
              >
                Clear Cart
              </Button>
            </div>
          </Card>
        </div>
      </div>
    </div>
  )
}
