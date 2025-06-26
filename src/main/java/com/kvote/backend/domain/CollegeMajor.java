package com.kvote.backend.domain;

public enum CollegeMajor {
    ALL("전체학과", "전체"),
    GERMAN("글로벌어문학부", "독어독문전공"),
    FRENCH("글로벌어문학부", "프랑스어문전공"),
    JAPANESE("글로벌어문학부", "일어일문전공"),
    CHINESE("글로벌어문학부", "중어중문전공"),
    RUSSIAN("글로벌어문학부", "러시아어문전공"),
    VISUAL_DESIGN("디자인비즈학부", "시각정보디자인전공"),
    INDUSTRIAL_DESIGN("디자인비즈학부", "산업디자인전공"),
    JEWELRY_DESIGN("디자인비즈학부", "장신구금속디자인전공"),
    KOREAN_PAINTING("Fine Arts학부", "한국화전공"),
    WESTERN_PAINTING("Fine Arts학부", "서양화전공"),
    ARTS_MANAGEMENT("Fine Arts학부", "미술경영전공"),
    CALLIGRAPHY("Fine Arts학부", "서예전공"),
    SPORTS_HEALTH("스포츠과학부", "스포츠건강과학전공"),
    SPORTS_LEISURE("스포츠과학부", "스포츠레저산업전공"),
    CRIMINAL_PSYCHOLOGY("공공안전학부", "범죄교정심리학전공"),
    POLICE_ADMINISTRATION("공공안전학부", "경찰행정학전공"),
    SOCIAL_WELFARE("휴먼서비스학부", "사회복지학전공"),
    YOUTH_STUDIES("휴먼서비스학부", "청소년학전공"),
    PUBLIC_ADMINISTRATION("공공인재학부", "행정학전공"),
    POLITICAL_SCIENCE("공공인재학부", "정치외교학전공"),
    ECONOMICS("경제학부", "경제학전공"),
    APPLIED_STATISTICS("경제학부", "응용통계학전공"),
    INTELLECTUAL_PROPERTY("경제학부", "지식재산학전공"),
    BUSINESS_ADMINISTRATION("경영학부", "경영학전공"),
    ACCOUNTING("경영학부", "회계세무학전공"),
    COMPUTER_ENGINEERING("AI컴퓨터공학부", "컴퓨터공학전공"),
    ARTIFICIAL_INTELLIGENCE("AI컴퓨터공학부", "인공지능전공"),
    SOFTWARE_SECURITY("AI컴퓨터공학부", "SW안전보안전공"),
    MOBILITY_SOFTWARE("AI컴퓨터공학부", "모빌리티SW전공"),
    LIFE_SCIENCE("바이오융합학부", "생명과학전공"),
    FOOD_BIOTECH("바이오융합학부", "식품생물공학전공"),
    NANO_SEMICONDUCTOR("전자공학부", "나노·반도체전공"),
    INFORMATION_COMMUNICATION("전자공학부", "정보통신시스템전공"),
    MATERIAL_ENGINEERING("신소재화학공학부", "신소재공학전공"),
    CHEMICAL_ENGINEERING("신소재화학공학부", "화학공학전공"),
    ARCHITECTURAL_ENGINEERING("스마트시티공학부", "건축공학전공"),
    URBAN_TRANSPORT("스마트시티공학부", "도시·교통공학전공"),
    HOTEL_MANAGEMENT("호텔외식경영학부", "호텔경영전공"),
    FOOD_COOKERY("호텔외식경영학부", "외식·조리전공");

    private final String department;
    private final String major;

    CollegeMajor(String department, String major) {
        this.department = department;
        this.major = major;
    }

    public String getDepartment() {
        return department;
    }

    public String getMajor() {
        return major;
    }
}