package fiap.restaurant.app.adapter.presenter;

import fiap.restaurant.app.adapter.web.json.menuitem.MenuItemDTO;
import fiap.restaurant.app.adapter.web.json.menuitem.UpsertMenuItemDTO;
import fiap.restaurant.app.core.domain.MenuItem;

public interface MenuItemPresenter {
    MenuItemDTO toDTO(MenuItem menuItem);
    MenuItem toDomain(UpsertMenuItemDTO dto);
}