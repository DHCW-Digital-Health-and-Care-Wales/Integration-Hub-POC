output "storage_account_name" {
  value = azurerm_storage_account.terraform_state.name
}

output "storage_account_id" {
  value = azurerm_storage_account.terraform_state.id
}

output "container_name" {
  value = azurerm_storage_container.terraform_state.name
}
