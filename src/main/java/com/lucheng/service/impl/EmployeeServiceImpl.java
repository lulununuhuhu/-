package com.lucheng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucheng.common.R;
import com.lucheng.constants.SystemConstant;
import com.lucheng.domain.Employee;
import com.lucheng.service.EmployeeService;
import com.lucheng.mapper.EmployeeMapper;
import com.lucheng.utils.BeanCopyUtils;
import com.lucheng.vo.EmployeeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @author lucheng
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2023-01-05 22:03:09
*/
@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
implements EmployeeService{

    @Override
    public R<EmployeeVo> login(HttpServletRequest request,Employee employee) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //根据用户名查询数据库
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee res = getOne(queryWrapper);
        if(ObjectUtils.isEmpty(res)){
            return R.error(SystemConstant.USERNAME_NOT_EXISTS);
        }
        //根据用户名查询密码
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());//将原始密码进行md5加密
        queryWrapper.eq(Employee::getPassword,password);
        res = getOne(queryWrapper);
        if(ObjectUtils.isEmpty(res))
            return R.error(SystemConstant.PASSWORD_WRONG);

        //查询用户状态
        if(res.getStatus().equals(SystemConstant.STATUS_BANNED))
            return R.error(SystemConstant.USER_BANNED);

        //登录成功，将员工id存入Session并返回登录结果
        request.getSession().setAttribute("employee",res.getId());
        //将res封装成EmployeeVo
        EmployeeVo employeeVo = BeanCopyUtils.copyBean(res, EmployeeVo.class);
        return R.success(employeeVo);
    }

    @Override
    public R<String> add(Employee employee) {
        log.info("新增员工,员工信息： {}",employee.toString());
        //密码默认是初始密码123456
        String password = "123456";
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        employee.setPassword(password);
        save(employee);
        return R.success("添加员工成功");
    }

    @Override
    public R<Page> queryEmployeeByPage(Integer page, Integer pageSize, String name) {
        Page<Employee> employeePage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Employee::getName,name);
        Page<Employee> res = page(employeePage, queryWrapper);
        return R.success(res);
    }

    @Override
    public R<String> updateEmployee(Employee employee) {
        boolean res = updateById(employee);
        if(res == true)
            return R.success("修改状态成功");
        else
            return R.error("修改状态失败");
    }
}
