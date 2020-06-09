package com.yueny.study.console.service.impl;

import com.yueny.study.console.service.AbstractService;
import com.yueny.study.console.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-10-31 19:09
 */
@Service
public class UserServiceImpl extends AbstractService implements IUserService {

    @Override
    public List<String> getList() {
        return Arrays.asList("1", "6", "8");
    }
}
