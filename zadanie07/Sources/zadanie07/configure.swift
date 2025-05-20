import Fluent
import FluentSQLiteDriver
import Leaf
import Vapor
import Redis

// configures your application
public func configure(_ app: Application) async throws {
    // uncomment to serve files from /Public folder
    app.middleware.use(FileMiddleware(publicDirectory: app.directory.publicDirectory))

    app.databases.use(DatabaseConfigurationFactory.sqlite(.file("db.sqlite")), as: .sqlite)

    app.migrations.add(CreateCategory())
    app.migrations.add(CreateProduct())
    app.migrations.add(CreateTag())
    app.migrations.add(CreateProductTagPivot())

    if let redisURL = Environment.get("REDIS_URL") {
        app.logger.info("REDIS_URL found. Configuring Heroku Redis.")
        var config = try RedisConfiguration(url: redisURL)

        config.tlsConfiguration = .forClient(certificateVerification: .none)

        app.redis.configuration = config
    } else {
        do {
            app.logger.info("No REDIS_URL found. Attempting local Redis (hostname: redis).")
            app.redis.configuration = try RedisConfiguration(hostname: "redis", port: 6379)
        } catch {
            app.logger.warning("Failed to connect to Redis at 'redis'. Falling back to 'localhost'. Error: \(error)")
            app.redis.configuration = try RedisConfiguration(hostname: "localhost", port: 6379)
        }
    }
    app.views.use(.leaf)


    // register routes
    try routes(app)
}
