package fiap.restaurant.app.adapter.presenter;

import fiap.restaurant.app.adapter.database.jpa.entity.BusinessHoursEntity;
import fiap.restaurant.app.core.domain.BusinessHours;

public interface BusinessHoursPresenter {

    BusinessHours toDomain(BusinessHoursEntity entity);

    BusinessHoursEntity toEntity(BusinessHours domain, BusinessHoursEntity entity);
} 