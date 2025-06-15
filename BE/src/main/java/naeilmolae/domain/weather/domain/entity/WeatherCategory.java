package naeilmolae.domain.weather.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WeatherCategory {
    PTY("강수 형태", "0: 없음, 1: 비, 2: 비/눈, 3: 눈"),
    REH("상대습도 (%)", "현재 상대습도"),
    RN1("1시간 강수량 (mm)", "1시간 강수량"),
    T1H("기온 (℃)", "현재 기온"),
    UUU("동서바람성분 (m/s)", "동서바람성분"),
    VEC("풍향 (°)", "풍향"),
    VVV("남북바람성분 (m/s)", "남북바람성분"),
    WSD("풍속 (m/s)", "풍속");

    private final String description; // 기상 요소 설명
    private final String details;     // 상세 정보 설명

    /**
     * 카테고리 코드로 Enum 찾기
     *
     * @param code 카테고리 코드 (e.g., "PTY")
     * @return WeatherCategory
     */
    public static WeatherCategory fromCode(String code) {
        for (WeatherCategory category : WeatherCategory.values()) {
            if (category.name().equalsIgnoreCase(code)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown WeatherCategory code: " + code);
    }
}
