package fiap.restaurant.app.adapter.presenter;

import fiap.restaurant.app.adapter.database.jpa.entity.AddressEntity;
import fiap.restaurant.app.adapter.web.json.common.AddressDTO;
import fiap.restaurant.app.core.domain.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressPresenterImpl implements AddressPresenter {

    @Override
    public Address mapToDomain(AddressDTO addressDTO) {
        if (addressDTO == null) {
            return null;
        }

        Address.AddressBuilder address = Address.builder();

        address.id(addressDTO.getId());
        address.street(addressDTO.getStreet());
        address.city(addressDTO.getCity());
        address.state(addressDTO.getState());
        address.zipCode(addressDTO.getZipCode());
        address.country(addressDTO.getCountry());

        return address.build();
    }

    @Override
    public AddressDTO mapToDTO(Address address) {
        if (address == null) {
            return null;
        }

        AddressDTO addressDTO = new AddressDTO();

        addressDTO.setId(address.getId());
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setZipCode(address.getZipCode());
        addressDTO.setCountry(address.getCountry());

        return addressDTO;
    }

    @Override
    public Address mapToDomain(AddressEntity addressEntity) {
        if (addressEntity == null) {
            return null;
        }

        Address.AddressBuilder address = Address.builder();

        address.id(addressEntity.getId());
        address.street(addressEntity.getStreet());
        address.city(addressEntity.getCity());
        address.state(addressEntity.getState());
        address.zipCode(addressEntity.getZipCode());
        address.country(addressEntity.getCountry());

        return address.build();
    }
}
