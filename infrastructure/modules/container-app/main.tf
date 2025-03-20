resource "azurerm_container_app_environment" "main" {
  name                       = var.container_app_environment_name
  location                   = var.location
  resource_group_name        = var.resource_group_name
  log_analytics_workspace_id = azurerm_log_analytics_workspace.main.id
}

resource "azurerm_log_analytics_workspace" "main" {
  name                = "${var.container_app_environment_name}-logs"
  location            = var.location
  resource_group_name = var.resource_group_name
  sku                 = "PerGB2018"
  retention_in_days   = 30
}

data "azurerm_client_config" "current" {}

resource "azurerm_container_app" "main" {
  name                         = var.container_app_name
  container_app_environment_id = azurerm_container_app_environment.main.id
  resource_group_name          = var.resource_group_name
  revision_mode                = "Single"

  identity {
    type = "SystemAssigned"
  }

  registry {
    server               = var.container_registry_login_server
    identity             = "System"
  }

  template {
    container {
      name   = var.container_app_name
      image  = "${var.container_registry_login_server}/${var.container_app_image}"
      cpu    = 0.5
      memory = "1Gi"

      env {
        name  = "SERVICEBUS_NAMESPACE"
        value = var.service_bus_namespace_name
      }

      env {
        name  = "SERVICEBUS_QUEUE_NAME"
        value = var.service_bus_queue_name
      }

      env {
        name  = "SERVICEBUS_TOPIC_NAME"
        value = var.service_bus_topic_name
      }

      env {
        name  = "SERVICEBUS_SUBSCRIPTION_NAME"
        value = var.service_bus_subscription_name
      }
    }

    min_replicas = 1
    max_replicas = 3
  }

  tags = {
    Environment = var.environment
  }
}

# Role assignments for Service Bus
resource "azurerm_role_assignment" "service_bus_data_owner" {
  scope                = var.service_bus_namespace_id
  role_definition_name = "Azure Service Bus Data Owner"
  principal_id         = azurerm_container_app.main.identity[0].principal_id

  depends_on = [
    azurerm_container_app.main
  ]
}

# Role assignments for ACR
resource "azurerm_role_assignment" "acr_pull" {
  scope                = var.container_registry_id
  role_definition_name = "AcrPull"
  principal_id         = azurerm_container_app.main.identity[0].principal_id

  depends_on = [
    azurerm_container_app.main
  ]
}
