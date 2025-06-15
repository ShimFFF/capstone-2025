package naeilmolae.domain.alarm.domain.entity;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum AlarmCategory {
    // 루트 카테고리들
    WAKE_UP("기상", CategoryType.DAILY, null, "일어날 때가 된 청년에게,\n아침을 깨우는 한 마디."),
    GO_OUT("외출", CategoryType.DAILY, null, "집을 나서는 청년에게,\n배웅하는 말 한 마디."),
    MEAL("식사", CategoryType.DAILY, null, "식사할 때가 된 청년에게,\n밥 챙겨 먹으라는 말 한 마디."),
    SLEEP("취침", CategoryType.DAILY, null, "하루를 마무리하는 청년에게,\n잘 자라는 한 마디."),
    SADNESS("우울과 불안", CategoryType.COMFORT, null, "우울하고 불안한 청년에게,\n괜찮다는 다독임의 말."),
    PRAISE("칭찬과 격려", CategoryType.COMFORT, null, "칭찬이 필요한 청년에게,\n잘 하고 있다는 격려의 말."),
    CONSOLATION("위로", CategoryType.COMFORT, null, "삶이 어려운 청년에게,\n경험을 담은 위로의 말."),
    INFO("정보", CategoryType.INFO, null, "아이들에게 정보를 전달해주세요."),

    // 자식 카테고리들 - WAKE_UP 관련
    WAKE_UP_WEEKDAY("아침 기상 (평일)", CategoryType.DAILY, WAKE_UP, "하루를 시작할 수 있도록 깨워주세요."),
    WAKE_UP_WEEKEND("아침 기상 (주말)", CategoryType.DAILY, WAKE_UP, "주말 아침에 일어나야하는 청년을 깨워주세요."),

    // 자식 카테고리들 - GO_OUT 관련
    GO_OUT_CLEAR("날씨가 맑을 때", CategoryType.DAILY, GO_OUT, "맑은 날씨를 전하며 배웅해주세요."),
    GO_OUT_RAIN("비가 올 때", CategoryType.DAILY, GO_OUT, "우산을 챙기라는 말을 해주세요."),
    GO_OUT_SNOW("눈이 올 때", CategoryType.DAILY, GO_OUT, "눈 오는 날 조심하라는 말을 해주세요."),
    GO_OUT_COLD("날씨가 추울 때", CategoryType.DAILY, GO_OUT, "따뜻하게 입으라는 말을 해주세요."),
    GO_OUT_HOT("날씨가 더울 때", CategoryType.DAILY, GO_OUT, "더운 날씨에 유의하라는 말을 해주세요."),

    // 자식 카테고리들 - MEAL 관련
    MEAL_BREAKFAST("아침 식사 알림", CategoryType.DAILY, MEAL, "아침 식사 챙겨먹으라는 말을 해주세요."),
    MEAL_LUNCH("점심 식사 알림", CategoryType.DAILY, MEAL, "점심 식사 챙겨먹으라는 말을 해주세요."),
    MEAL_DINNER("저녁 식사 알림", CategoryType.DAILY, MEAL, "저녁 식사 챙겨먹으라는 말을 해주세요."),

    // 자식 카테고리들 - SLEEP 관련
    SLEEP_PREPARE("취침 준비", CategoryType.DAILY, SLEEP, "하루를 마무리 하는 밤인사를 해주세요."),

    // 자식 카테고리들 - SADNESS 관련
    SADNESS_ALONE("혼자 있을 때", CategoryType.COMFORT, SADNESS, "혼자 있는 청년에게 따뜻한 말을 해주세요."),
    SADNESS_BEFORE_SLEEP("밤에 잠들기 전", CategoryType.COMFORT, SADNESS, "청년의 불안함을 잠재워줄 말을 해주세요."),
    SADNESS_LONELY("외로움을 느낄 때", CategoryType.COMFORT, SADNESS, "외로울 때 힘이 될 따뜻한 말을 해주세요."),
    SADNESS_OVERWHELMED("막막함을 느낄 때", CategoryType.COMFORT, SADNESS, "막막함을 느끼는 청년에게 희망을 주세요."),
    SADNESS_UNCERTAIN_FUTURE("미래가 불안할 때", CategoryType.COMFORT, SADNESS, "미래에 대한 불안을 덜어주는 말을 해주세요."),

    // 자식 카테고리들 - PRAISE 관련
    PRAISE_SMALL_ACHIEVEMENT("작은 성취를 이루었을 때", CategoryType.COMFORT, PRAISE, "성취를 이룬 청년을 축하하고 칭찬해주세요."),
    PRAISE_EFFORTING("목표를 향해 노력 중일 때", CategoryType.COMFORT, PRAISE, "노력을 인정하는 말을 해주세요."),
    PRAISE_OVERCOME("어려움을 극복해냈을 때", CategoryType.COMFORT, PRAISE, "어려움을 이겨낸 청년을 인정해주세요."),
    PRAISE_PERSISTENCE("꾸준히 노력 중일 때", CategoryType.COMFORT, PRAISE, "꾸준히 노력하는 청년을 응원해주세요."),
    PRAISE_TIRED("지쳐 있을 때", CategoryType.COMFORT, PRAISE, "지친 마음에 공감하는 말을 해주세요."),

    // 자식 카테고리들 - CONSOLATION 관련
    CONSOLATION_MISTAKE("실수를 했을 때", CategoryType.COMFORT, CONSOLATION, "실수를 받아들일 수 있도록 격려해주세요."),
    CONSOLATION_PLAN_FAIL("계획대로 되지 않았을 때", CategoryType.COMFORT, CONSOLATION, "일이 뜻대로 되지 않는 청년을 격려해주세요."),
    CONSOLATION_FRUSTRATION("좌절감을 느낄 때", CategoryType.COMFORT, CONSOLATION, "무거운 마음을 다독여주세요."),
    CONSOLATION_FATIGUE("피로를 느낄 때", CategoryType.COMFORT, CONSOLATION, "지친 몸과 마음을 보살피는 말을 해주세요."),
    CONSOLATION_INADEQUATE("스스로가 부족하다고 느낄 때", CategoryType.COMFORT, CONSOLATION, "스스로를 사랑할 수 있도록 응원해주세요."),

    // 자식 카테고리들 - INFO 관련
    INFO_INFO("정보", CategoryType.DAILY, INFO, "아이들에게 정보를 전달해주세요.");

    private final String name;
    private final CategoryType categoryType;
    private final AlarmCategory parent;
    private final String title;

    // 모든 루트 카테고리 상수로 저장
    public static final List<AlarmCategory> ROOT_CATEGORIES;

    static {
        // 루트 카테고리 초기화
        ROOT_CATEGORIES = Stream.of(AlarmCategory.values())
                .filter(AlarmCategory::isRoot)
                .collect(Collectors.toList());
    }

    AlarmCategory(String name, CategoryType categoryType, AlarmCategory parent, String title) {
        this.name = name;
        this.categoryType = categoryType;
        this.parent = parent;
        this.title = title;
    }

    public static List<AlarmCategory> getByParent(AlarmCategory parent) {
        return List.of(AlarmCategory.values()).stream()
                .filter(category -> category.isChildOf(parent))
                .toList();
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isChildOf(AlarmCategory parentCategory) {
        return this.parent == parentCategory;
    }

    public static List<AlarmCategory> getChildrenByParent(AlarmCategory parent) {
        return Stream.of(AlarmCategory.values())
                .filter(category -> category.isChildOf(parent))
                .collect(Collectors.toList());
    }
}
