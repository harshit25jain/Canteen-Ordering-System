import React, { useState, useEffect } from 'react'
import { Card } from '../components/ui/Card'
import { Button } from '../components/ui/Button'
import { Badge } from '../components/ui/Badge'
import { Input } from '../components/ui/Input'
import { Search, ShoppingCart, Plus, Minus } from 'lucide-react'
import { menuService } from '../../lib/services/menu-service'
import { useCart } from '../../lib/hooks/use-cart'
import { toast } from 'sonner'

export default function MenuPage() {
  const [menuItems, setMenuItems] = useState([])
  const [filteredItems, setFilteredItems] = useState([])
  const [searchQuery, setSearchQuery] = useState('')
  const [loading, setLoading] = useState(true)
  const { addItem, items: cartItems } = useCart()

  useEffect(() => {
    fetchMenuItems()
  }, [])

  useEffect(() => {
    filterItems()
  }, [menuItems, searchQuery])

  const fetchMenuItems = async () => {
    try {
      const items = await menuService.getAvailableItems()
      setMenuItems(items)
    } catch (error) {
      toast.error('Failed to load menu items')
    } finally {
      setLoading(false)
    }
  }

  const filterItems = () => {
    if (!searchQuery) {
      setFilteredItems(menuItems)
    } else {
      const filtered = menuItems.filter(item =>
        item.name.toLowerCase().includes(searchQuery.toLowerCase())
      )
      setFilteredItems(filtered)
    }
  }

  const handleAddToCart = (item) => {
    if (item.stockCount > 0) {
      addItem({
        menuItemId: item.id,
        name: item.name,
        price: item.price,
        quantity: 1,
        stockCount: item.stockCount,
      })
      toast.success(`${item.name} added to cart`)
    } else {
      toast.error('Item out of stock')
    }
  }

  const getCartQuantity = (itemId) => {
    const cartItem = cartItems.find(item => item.menuItemId === itemId)
    return cartItem ? cartItem.quantity : 0
  }

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[...Array(6)].map((_, i) => (
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
        <h1 className="text-4xl font-bold mb-4">Our Menu</h1>
        <p className="text-muted-foreground mb-6">
          Fresh ingredients, authentic flavors, and real-time availability
        </p>
        
        <div className="relative max-w-md">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
          <Input
            placeholder="Search menu items..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-10"
          />
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredItems.map((item) => {
          const cartQuantity = getCartQuantity(item.id)
          const isOutOfStock = item.stockCount === 0

          return (
            <Card key={item.id} className="p-6 hover:shadow-lg transition-shadow">
              <div className="space-y-4">
                <div className="flex justify-between items-start">
                  <h3 className="text-xl font-semibold">{item.name}</h3>
                  <Badge variant={isOutOfStock ? "destructive" : "secondary"}>
                    {isOutOfStock ? "Out of Stock" : `${item.stockCount} left`}
                  </Badge>
                </div>
                
                <p className="text-2xl font-bold text-primary">
                  ${item.price.toFixed(2)}
                </p>

                <div className="flex items-center justify-between">
                  {cartQuantity > 0 ? (
                    <div className="flex items-center gap-2">
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => {
                          // Handle quantity decrease
                          toast.info('Use cart to modify quantities')
                        }}
                      >
                        <Minus className="h-4 w-4" />
                      </Button>
                      <span className="font-medium">{cartQuantity}</span>
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => handleAddToCart(item)}
                        disabled={isOutOfStock}
                      >
                        <Plus className="h-4 w-4" />
                      </Button>
                    </div>
                  ) : (
                    <Button
                      onClick={() => handleAddToCart(item)}
                      disabled={isOutOfStock}
                      className="w-full"
                    >
                      <ShoppingCart className="mr-2 h-4 w-4" />
                      {isOutOfStock ? 'Out of Stock' : 'Add to Cart'}
                    </Button>
                  )}
                </div>
              </div>
            </Card>
          )
        })}
      </div>

      {filteredItems.length === 0 && (
        <div className="text-center py-12">
          <p className="text-muted-foreground text-lg">
            {searchQuery ? 'No items found matching your search.' : 'No menu items available.'}
          </p>
        </div>
      )}
    </div>
  )
}




