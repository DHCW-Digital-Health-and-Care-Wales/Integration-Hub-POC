resource "azurerm_servicebus_namespace" "main" {
  name                = var.service_bus_namespace
  location            = var.location
  resource_group_name = var.resource_group_name
  sku                 = "Standard"

  tags = {
    Environment = var.environment
  }
}

resource "azurerm_servicebus_queue" "main" {
  name         = var.service_bus_queue_name
  namespace_id = azurerm_servicebus_namespace.main.id

  enable_partitioning = true
  max_size_in_megabytes = 1024
  default_message_ttl  = "P14D"  # 14 days
}

resource "azurerm_servicebus_topic" "main" {
  name         = var.service_bus_topic_name
  namespace_id = azurerm_servicebus_namespace.main.id

  enable_partitioning = true
  max_size_in_megabytes = 1024
  default_message_ttl  = "P14D"  # 14 days
}

resource "azurerm_servicebus_subscription" "main" {
  name               = var.service_bus_subscription_name
  topic_id           = azurerm_servicebus_topic.main.id
  max_delivery_count = 10
  default_message_ttl = "P14D"  # 14 days
}
