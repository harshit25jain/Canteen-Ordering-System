import React, { useState, useEffect } from 'react'
import { Card } from '../components/ui/Card'
import { Button } from '../components/ui/Button'
import { Badge } from '../components/ui/Badge'
import { Input } from '../components/ui/Input'
import { Textarea } from '../components/ui/Textarea'
import { 
  TrendingUp, 
  Package, 
  ShoppingCart, 
  DollarSign,
  Plus,
  Edit,
  Trash2
} from 'lucide-react'
import { menuService } from '../../lib/services/menu-service'
import { orderService } from '../../lib/services/order-service'

import { toast } from 'sonner'

export default function AdminPage() {
  const [menuItems, setMenuItems] = useState([])
  const [orders, setOrders] = useState([])
  const [stats, setStats] = useState({
    totalItems: 0,
    totalOrders: 0,
    pendingOrders: 0,
    totalRevenue: 0
  })
  const [loading, setLoading] = useState(true)
  const [showAddForm, setShowAddForm] = useState(false)
  const [newItem, setNewItem] = useState({
    name: '',
    price: '',
    stockCount: '',
    description: ''
  })

  useEffect(() => {
    fetchData()
  }, [])

  const fetchData = async () => {
    try {
      const [menuData, orderData, pendingOrders] = await Promise.all([
        menuService.getAllItems(),
        orderService.getAllOrders(),
        orderService.getPendingOrders()
      ])
      
      setMenuItems(menuData)
      setOrders(orderData)
      
      const totalRevenue = orderData
        .filter(order => order.status === 'PAID')
        .reduce((sum, order) => sum + order.menuItem.price, 0)
      
      setStats({
        totalItems: menuData.length,
        totalOrders: orderData.length,
        pendingOrders: pendingOrders.length,
        totalRevenue
      })
    } catch (error) {
      toast.error('Failed to load admin data')
    } finally {
      setLoading(false)
    }
  }

  const handleAddItem = async (e) => {
    e.preventDefault()
    try {
      await menuService.createItem({
        name: newItem.name,
        price: parseFloat(newItem.price),
        stockCount: parseInt(newItem.stockCount),
        description: newItem.description
      })
      
      toast.success('Menu item added successfully!')
      setNewItem({ name: '', price: '', stockCount: '', description: '' })
      setShowAddForm(false)
      fetchData()
    } catch (error) {
      toast.error('Failed to add menu item')
    }
  }

  const handleDeleteItem = async (id) => {
    if (window.confirm('Are you sure you want to delete this item?')) {
      try {
        await menuService.deleteItem(id)
        toast.success('Menu item deleted successfully!')
        fetchData()
      } catch (error) {
        toast.error('Failed to delete menu item')
      }
    }
  }

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          {[...Array(4)].map((_, i) => (
            <Card key={i} className="p-6 animate-pulse">
              <div className="h-4 bg-muted rounded mb-4"></div>
              <div className="h-8 bg-muted rounded"></div>
            </Card>
          ))}
        </div>
      </div>
    )
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-8">
        <h1 className="text-4xl font-bold mb-4">Admin Dashboard</h1>
        <p className="text-muted-foreground">
          Manage your canteen operations and monitor performance
        </p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <Card className="p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-muted-foreground">Total Items</p>
              <p className="text-2xl font-bold">{stats.totalItems}</p>
            </div>
            <Package className="h-8 w-8 text-primary" />
          </div>
        </Card>

        <Card className="p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-muted-foreground">Total Orders</p>
              <p className="text-2xl font-bold">{stats.totalOrders}</p>
            </div>
            <ShoppingCart className="h-8 w-8 text-primary" />
          </div>
        </Card>

        <Card className="p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-muted-foreground">Pending Orders</p>
              <p className="text-2xl font-bold">{stats.pendingOrders}</p>
            </div>
            <TrendingUp className="h-8 w-8 text-primary" />
          </div>
        </Card>

        <Card className="p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-muted-foreground">Total Revenue</p>
              <p className="text-2xl font-bold">${stats.totalRevenue.toFixed(2)}</p>
            </div>
            <DollarSign className="h-8 w-8 text-primary" />
          </div>
        </Card>
      </div>

      {/* Menu Management */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <Card className="p-6">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-2xl font-bold">Menu Items</h2>
            <Button onClick={() => setShowAddForm(true)}>
              <Plus className="mr-2 h-4 w-4" />
              Add Item
            </Button>
          </div>

          {showAddForm && (
            <form onSubmit={handleAddItem} className="mb-6 p-4 border rounded-lg">
              <h3 className="text-lg font-semibold mb-4">Add New Menu Item</h3>
              <div className="space-y-4">
                <Input
                  placeholder="Item name"
                  value={newItem.name}
                  onChange={(e) => setNewItem({...newItem, name: e.target.value})}
                  required
                />
                <div className="grid grid-cols-2 gap-4">
                  <Input
                    type="number"
                    placeholder="Price"
                    value={newItem.price}
                    onChange={(e) => setNewItem({...newItem, price: e.target.value})}
                    required
                  />
                  <Input
                    type="number"
                    placeholder="Stock count"
                    value={newItem.stockCount}
                    onChange={(e) => setNewItem({...newItem, stockCount: e.target.value})}
                    required
                  />
                </div>
                <Textarea
                  placeholder="Description (optional)"
                  value={newItem.description}
                  onChange={(e) => setNewItem({...newItem, description: e.target.value})}
                />
                <div className="flex gap-2">
                  <Button type="submit">Add Item</Button>
                  <Button type="button" variant="outline" onClick={() => setShowAddForm(false)}>
                    Cancel
                  </Button>
                </div>
              </div>
            </form>
          )}

          <div className="space-y-4">
            {menuItems.map((item) => (
              <div key={item.id} className="flex items-center justify-between p-4 border rounded-lg">
                <div>
                  <h3 className="font-semibold">{item.name}</h3>
                  <p className="text-sm text-muted-foreground">
                    ${item.price.toFixed(2)} • {item.stockCount} in stock
                  </p>
                </div>
                <div className="flex gap-2">
                  <Button size="sm" variant="outline">
                    <Edit className="h-4 w-4" />
                  </Button>
                  <Button 
                    size="sm" 
                    variant="destructive"
                    onClick={() => handleDeleteItem(item.id)}
                  >
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
              </div>
            ))}
          </div>
        </Card>

        <Card className="p-6">
          <h2 className="text-2xl font-bold mb-6">Recent Orders</h2>
          <div className="space-y-4">
            {orders.slice(0, 5).map((order) => (
              <div key={order.id} className="flex items-center justify-between p-4 border rounded-lg">
                <div>
                  <h3 className="font-semibold">Order #{order.id}</h3>
                  <p className="text-sm text-muted-foreground">{order.menuItem.name}</p>
                </div>
                <div className="text-right">
                  <Badge className={order.status === 'PAID' ? 'bg-green-100 text-green-800' : 
                                   order.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' : 
                                   'bg-red-100 text-red-800'}>
                    {order.status}
                  </Badge>
                  <p className="text-sm font-semibold">${order.menuItem.price.toFixed(2)}</p>
                </div>
              </div>
            ))}
          </div>
        </Card>
      </div>
    </div>
  )
}


