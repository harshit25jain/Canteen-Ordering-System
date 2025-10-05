// Type definitions for the application
// Note: In JavaScript, we use JSDoc comments for type hints

/**
 * @typedef {Object} MenuItem
 * @property {number} id
 * @property {string} name
 * @property {string} [description]
 * @property {number} price
 * @property {number} stockCount
 * @property {string} [category]
 * @property {string} [imageUrl]
 * @property {string} [createdAt]
 * @property {string} [updatedAt]
 */

/**
 * @typedef {Object} Order
 * @property {number} id
 * @property {MenuItem} menuItem
 * @property {"PENDING" | "PAID" | "CANCELLED"} status
 * @property {number} totalPrice
 * @property {string} createdAt
 * @property {string} updatedAt
 */

/**
 * @typedef {Object} CartItem
 * @property {number} menuItemId
 * @property {string} name
 * @property {number} price
 * @property {number} quantity
 * @property {number} stockCount
 * @property {string} [imageUrl]
 */

/**
 * @typedef {Object} ApiResponse
 * @property {*} [data]
 * @property {string} [error]
 * @property {string} [message]
 */

/**
 * @typedef {Object} OrderStats
 * @property {number} totalOrders
 * @property {number} pendingOrders
 * @property {number} paidOrders
 * @property {number} cancelledOrders
 * @property {number} totalRevenue
 */

// Export empty object to maintain module structure
export default {};









