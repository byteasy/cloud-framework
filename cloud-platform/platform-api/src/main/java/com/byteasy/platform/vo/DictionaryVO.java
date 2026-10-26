package com.byteasy.platform.vo;

import com.byteasy.platform.entity.Dictionary;
import com.byteasy.platform.entity.DictionaryItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@Schema(name = "DictionaryVO", description = "字典信息")
public class DictionaryVO extends Dictionary {
    @Serial
    private static final long serialVersionUID = 5448890593750052142L;

    @Schema(description = "字典值")
    private List<DictionaryItem> items;
}
