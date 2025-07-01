package com.javaAi;

import com.github.houbb.sensitive.word.api.IWordContext;
import com.github.houbb.sensitive.word.api.IWordReplace;
import com.github.houbb.sensitive.word.api.IWordResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyWordReplace implements IWordReplace {


    @Override
    public void replace(StringBuilder stringBuilder, char[] chars, IWordResult iWordResult, IWordContext iWordContext) {

    }
}
