output "name" {
  value = azurerm_container_app.main.name
}

output "id" {
  value = azurerm_container_app.main.id
}

output "url" {
  value = azurerm_container_app.main.latest_revision_fqdn
}

output "managed_identity_principal_id" {
  value = azurerm_container_app.main.identity[0].principal_id
}

output "managed_identity_tenant_id" {
  value = azurerm_container_app.main.identity[0].tenant_id
}
