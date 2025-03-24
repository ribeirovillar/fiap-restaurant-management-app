package fiap.restaurant.app.adapter.web.json;

public record ErrorResponseDto(String message, String method, String path, String timestamp, int status) {
}
