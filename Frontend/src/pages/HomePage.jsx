import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { Button } from '../components/ui/Button'
import { Card } from '../components/ui/Card'
import { ArrowRight, Clock, Shield, Zap, TrendingUp } from 'lucide-react'
import { menuService } from '../../lib/services/menu-service'
import { orderService } from '../../lib/services/order-service'

export default function HomePage() {
  const [stats, setStats] = useState({
    availableItems: 0,
    totalOrders: 0,
    isLoading: true,
  })

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [available, orders] = await Promise.all([
          menuService.getAvailableItems(),
          orderService.getAllOrders()
        ])
        setStats({
          availableItems: available.length,
          totalOrders: orders.length,
          isLoading: false,
        })
      } catch (error) {
        setStats((prev) => ({ ...prev, isLoading: false }))
      }
    }

    fetchStats()
  }, [])

  return (
    <div className="min-h-screen bg-background">
      {/* Hero Section */}
      <section className="relative min-h-[90vh] flex items-center justify-center px-6 overflow-hidden">
        {/* Background Pattern */}
        <div className="absolute inset-0 bg-gradient-to-br from-primary/10 via-background to-background" />
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_30%_50%,rgba(212,165,116,0.1),transparent_50%)]" />

        <div className="relative max-w-6xl mx-auto text-center space-y-12 animate-fade-in">
          {/* Badge */}
          <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full border border-primary/20 bg-primary/5 backdrop-blur-sm">
            <div className="w-2 h-2 rounded-full bg-primary animate-pulse" />
            <span className="text-sm font-medium text-foreground">Real-time Inventory System</span>
          </div>

          {/* Main Heading */}
          <h1 className="font-serif text-6xl md:text-8xl font-bold text-foreground leading-tight">
            Premium Canteen
            <span className="block gradient-text mt-2">Experience</span>
          </h1>

          {/* Subheading */}
          <p className="text-xl md:text-2xl text-muted-foreground max-w-3xl mx-auto leading-relaxed">
            Order from our curated menu with instant inventory updates, secure checkout, and automated order management
          </p>

          {/* CTA Buttons */}
          <div className="flex items-center justify-center gap-4 flex-wrap">
            <Button
              asChild
              size="lg"
              className="rounded-full px-8 h-14 text-base font-medium bg-primary hover:bg-primary/90 transition-all duration-300 hover:scale-105"
            >
              <Link to="/menu">
                Browse Menu
                <ArrowRight className="ml-2 w-5 h-5" />
              </Link>
            </Button>
            <Button
              asChild
              variant="outline"
              size="lg"
              className="rounded-full px-8 h-14 text-base font-medium border-border/50 hover:border-primary/50 transition-all duration-300 bg-transparent"
            >
              <Link to="/orders">View Orders</Link>
            </Button>
          </div>

          {/* Stats */}
          {!stats.isLoading && (
            <div className="flex items-center justify-center gap-8 pt-8 text-sm">
              <div className="flex items-center gap-2">
                <div className="w-8 h-8 rounded-full bg-primary/10 flex items-center justify-center">
                  <TrendingUp className="w-4 h-4 text-primary" />
                </div>
                <span className="text-muted-foreground">
                  <span className="font-semibold text-foreground">{stats.availableItems}</span> items available
                </span>
              </div>
              <div className="w-px h-6 bg-border" />
              <div className="flex items-center gap-2">
                <div className="w-8 h-8 rounded-full bg-primary/10 flex items-center justify-center">
                  <Clock className="w-4 h-4 text-primary" />
                </div>
                <span className="text-muted-foreground">
                  <span className="font-semibold text-foreground">{stats.totalOrders}</span> orders today
                </span>
              </div>
            </div>
          )}
        </div>

        {/* Scroll Indicator */}
        <div className="absolute bottom-8 left-1/2 -translate-x-1/2 animate-bounce">
          <div className="w-6 h-10 rounded-full border-2 border-primary/30 flex items-start justify-center p-2">
            <div className="w-1 h-3 rounded-full bg-primary animate-pulse" />
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-24 px-6">
        <div className="max-w-7xl mx-auto">
          <div className="text-center mb-16 space-y-4">
            <h2 className="font-serif text-4xl md:text-5xl font-bold text-foreground">Why Choose Infinite Locus</h2>
            <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
              Experience the future of canteen ordering with our innovative platform
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {/* Feature 1 */}
            <Card className="p-8 space-y-6 border-border/50 hover:border-primary/30 transition-all duration-500 hover:shadow-xl hover:shadow-primary/5 group">
              <div className="w-14 h-14 rounded-2xl bg-primary/10 flex items-center justify-center group-hover:scale-110 transition-transform duration-300">
                <Zap className="w-7 h-7 text-primary" />
              </div>
              <div className="space-y-3">
                <h3 className="font-serif text-2xl font-semibold text-foreground">Instant Updates</h3>
                <p className="text-muted-foreground leading-relaxed">
                  Real-time inventory tracking ensures you always see accurate stock levels, updated every 10 seconds
                </p>
              </div>
            </Card>

            {/* Feature 2 */}
            <Card className="p-8 space-y-6 border-border/50 hover:border-primary/30 transition-all duration-500 hover:shadow-xl hover:shadow-primary/5 group">
              <div className="w-14 h-14 rounded-2xl bg-primary/10 flex items-center justify-center group-hover:scale-110 transition-transform duration-300">
                <Clock className="w-7 h-7 text-primary" />
              </div>
              <div className="space-y-3">
                <h3 className="font-serif text-2xl font-semibold text-foreground">Smart Timers</h3>
                <p className="text-muted-foreground leading-relaxed">
                  15-minute countdown on pending orders with automatic cancellation to keep inventory flowing smoothly
                </p>
              </div>
            </Card>

            {/* Feature 3 */}
            <Card className="p-8 space-y-6 border-border/50 hover:border-primary/30 transition-all duration-500 hover:shadow-xl hover:shadow-primary/5 group">
              <div className="w-14 h-14 rounded-2xl bg-primary/10 flex items-center justify-center group-hover:scale-110 transition-transform duration-300">
                <Shield className="w-7 h-7 text-primary" />
              </div>
              <div className="space-y-3">
                <h3 className="font-serif text-2xl font-semibold text-foreground">Secure Checkout</h3>
                <p className="text-muted-foreground leading-relaxed">
                  Inventory locking during checkout prevents overselling and ensures your order is always fulfilled
                </p>
              </div>
            </Card>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-24 px-6">
        <div className="max-w-4xl mx-auto">
          <Card className="relative overflow-hidden border-border/50 bg-gradient-to-br from-primary/5 to-transparent">
            <div className="absolute inset-0 bg-[radial-gradient(circle_at_70%_30%,rgba(212,165,116,0.15),transparent_70%)]" />
            <div className="relative p-12 md:p-16 text-center space-y-8">
              <h2 className="font-serif text-4xl md:text-5xl font-bold text-foreground">Ready to Order?</h2>
              <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
                Browse our premium selection and experience seamless ordering
              </p>
              <Button
                asChild
                size="lg"
                className="rounded-full px-8 h-14 text-base font-medium bg-primary hover:bg-primary/90 transition-all duration-300 hover:scale-105"
              >
                <Link to="/menu">
                  Explore Menu
                  <ArrowRight className="ml-2 w-5 h-5" />
                </Link>
              </Button>
            </div>
          </Card>
        </div>
      </section>
    </div>
  )
}




