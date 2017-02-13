package com.anthem.oss.nimbus.core.domain.model.state.builder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by AF13233 on 9/8/16.
 */
@Getter
@Setter
@ToString
public class TemplateDefinition {
    private String path;
    private String id;
    private String criteria;

    public boolean canExecute(){
        return true;
    }
}
