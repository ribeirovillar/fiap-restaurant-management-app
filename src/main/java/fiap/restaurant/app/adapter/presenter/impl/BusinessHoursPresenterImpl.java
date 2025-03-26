package fiap.restaurant.app.adapter.presenter.impl;

import fiap.restaurant.app.adapter.database.jpa.entity.BusinessHoursEntity;
import fiap.restaurant.app.adapter.presenter.BusinessHoursPresenter;
import fiap.restaurant.app.core.domain.BusinessHours;
import org.springframework.stereotype.Component;

@Component
public class BusinessHoursPresenterImpl implements BusinessHoursPresenter {

    @Override
    public BusinessHours toDomain(BusinessHoursEntity entity) {
        if (entity == null) {
            return null;
        }

        return BusinessHours.builder()
                .id(entity.getId())
                .dayOfWeek(entity.getDayOfWeek())
                .openingTime(entity.getOpeningTime())
                .closingTime(entity.getClosingTime())
                .isClosed(entity.isClosed())
                .build();
    }

    @Override
    public BusinessHoursEntity toEntity(BusinessHours domain, BusinessHoursEntity entity) {
        if (domain == null) {
            return null;
        }

        if (entity == null) {
            entity = new BusinessHoursEntity();
        }

        entity.setDayOfWeek(domain.getDayOfWeek());
        entity.setOpeningTime(domain.getOpeningTime());
        entity.setClosingTime(domain.getClosingTime());
        entity.setClosed(domain.isClosed());

        return entity;
    }
} 