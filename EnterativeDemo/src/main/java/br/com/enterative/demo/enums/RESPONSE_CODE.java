package br.com.enterative.demo.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;

public enum RESPONSE_CODE {
	B00("00","responsecode.b00",0),
	B01("01","responsecode.b01",1),
	B02("02","responsecode.b02",2),
	B03("03","responsecode.b03",3),
	B04("04","responsecode.b04",4),
	B05("05","responsecode.b05",5),
	B06("06","responsecode.b06",6),
	B07("07","responsecode.b07",7),
	B08("08","responsecode.b08",8),
	B12("12","responsecode.b12",9),
	B13("13","responsecode.b13",10),
	B14("14","responsecode.b14",11),
	B15("15","responsecode.b15",12),
	B16("16","responsecode.b16",13),
	B17("17","responsecode.b17",14),
	B18("18","responsecode.b18",15),
	B20("20","responsecode.b20",16),
	B21("21","responsecode.b21",17),
	B22("22","responsecode.b22",18),
	B30("30","responsecode.b30",19),
	B33("33","responsecode.b33",20),
	B34("34","responsecode.b34",21),
	B35("35","responsecode.b35",22),
	B36("36","responsecode.b36",23),
	B37("37","responsecode.b37",24),
	B38("38","responsecode.b38",25),
	B41("41","responsecode.b41",26),
	B42("42","responsecode.b42",27),
	B43("43","responsecode.b43",28),
	B44("44","responsecode.b44",29),
	B51("51","responsecode.b51",30),
	B54("54","responsecode.b54",31),
	B55("55","responsecode.b55",32),
	B56("56","responsecode.b56",33),
	B58("58","responsecode.b58",34),
	B59("59","responsecode.b59",35),
	B61("61","responsecode.b61",36),
	B62("62","responsecode.b62",37),
	B65("65","responsecode.b65",38),
	B66("66","responsecode.b66",39),
	B69("69","responsecode.b69",40),
	B71("71","responsecode.b71",41),
	B74("74","responsecode.b74",42),
	B94("94","responsecode.b94",43),
	B95("95","responsecode.b95",44),
	B99("99","responsecode.b99",45),
	E00("E00","responsecode.e00",46),
	E01("E01","responsecode.e01",47),
	E02("E02","responsecode.e02",48),
	E03("E03","responsecode.e03",49),
	E04("E04","responsecode.e04",50),
	E05("E05","responsecode.e05",51),
	E06("E06","responsecode.e06",52),
	E07("E07","responsecode.e07",53),
	E08("E08","responsecode.e08",54),
	E09("E09","responsecode.e09",55),
	E10("E10","responsecode.e10",56),
	E11("E11","responsecode.e11",57),
	E12("E12","responsecode.e12",58),
	E13("E13","responsecode.e13",59),
	E14("E14","responsecode.e14",60),
	E15("E15","responsecode.e15",61),
	E16("E16","responsecode.e16",62),
	E99("E99","responsecode.e99",63),
	E17("E17","responsecode.e17",64),
	E18("E18","responsecode.e18",65),
	E19("E19","responsecode.e19",66),
	E20("E20","responsecode.e20",67),
	E21("E21","responsecode.e21",68),
	E22("E22","responsecode.e22",69),
	E23("E23","responsecode.e23",70),
	E24("E24","responsecode.e24",71),
	E25("E25","responsecode.e25",72),
	E26("E26","responsecode.e26",73),
	E27("E27","responsecode.e27",74),
	E28("E28","responsecode.e28",75),
	E29("E29","responsecode.e29",76),
	E30("E30","responsecode.e30",77),
	E31("E31","responsecode.e31",78);

    @Getter private final String code;
    @Getter private final String description;
    @Getter private final Integer sequence;

    RESPONSE_CODE(String code, String description, Integer sequence) {
        this.code = code;
        this.description = description;
        this.sequence = sequence;
    }

    public static RESPONSE_CODE get(String code) {
        RESPONSE_CODE[] values = RESPONSE_CODE.values();
        Optional<RESPONSE_CODE> result = Arrays.asList(values).stream().filter(r -> r.getCode().equals(code)).findFirst();
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

    public static List<RESPONSE_CODE> ordered() {
        return Arrays.stream(RESPONSE_CODE.values()).sorted(Comparator.comparing(RESPONSE_CODE::getSequence)).collect(Collectors.toList());
    }
}
