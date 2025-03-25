package fiap.restaurant.app.adapter.presenter;

import fiap.restaurant.app.adapter.database.jpa.entity.AddressEntity;
import fiap.restaurant.app.adapter.web.json.common.AddressDTO;
import fiap.restaurant.app.core.domain.Address;

public interface AddressPresenter {
    Address mapToDomain(AddressDTO addressDTO);

    AddressDTO mapToDTO(Address address);

    Address mapToDomain(AddressEntity addressEntity);
}
