package com.itheima.reggie.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.entity.dto.LoginDto;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @Author swk
 * @Date 2022/6/8 12:01
 * @Version 1.0
 * 员工控制类
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    HttpServletRequest request;

    /*登录功能*/
    @PostMapping("/login")
    public R login(@RequestBody LoginDto loginDto) {

        log.info("开始登录用户名：{}---密码：{}", loginDto.getUsername(), loginDto.getPassword());
        //1.获取传入的密码
        String password = loginDto.getPassword();
        if (StringUtils.isNotBlank(password)) {
            //密码进行md5加密.
            password = DigestUtils.md5DigestAsHex(password.getBytes());


            //判断员工是否存在
            LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(loginDto.getUsername() != null, Employee::getUsername, loginDto.getUsername());
            Employee employee = employeeService.getOne(queryWrapper);
            if (employee == null) {
                return R.fail("用户不存在");
            }
            //判断密码是否正确
            if (!employee.getPassword().equals(password)) {
                return R.fail("密码错误");
            }
            //判断员工是否被禁用
            if (employee.getStatus() == 0) {
                return R.fail("离职了，赶紧滚啊");
            }
            //登录成功，把员工信息存入session
            request.getSession().setAttribute("employee", employee.getId());
            //响应登录成功
            //登陆成功，需要将员工对象响应到前台，否则前台右上角看不到当前登录者的身份
            return R.success("登陆成功", employee);
        }

        return R.fail("登录失败");


    }


    /*退出功能
     * 返回操作结果*/
    @PostMapping("/logout")
    public R logout() {
        log.info("登出注销");
        request.getSession().removeAttribute("employee");
        return R.success("注销成功");
    }


    /*新增员工*/
    @PostMapping
    public R save(@RequestBody Employee employee) {

        //保存员工
        boolean saveResult = employeeService.saveWithCheckUserName(employee);
        if (saveResult) {
            return R.success("新增员工成功");
        }
        return R.fail("新增员工失败");
    }


    //分页查询
    @GetMapping("/page")
    public R<Page<Employee>> page(@RequestParam("page") Integer currentPage, Integer pageSize, String name) {
        //代码健壮性
        if (pageSize == null) {
            pageSize = 5;
        }
        if (currentPage == null) {
            currentPage = 1;
        }
        //组织分页对象
        Page<Employee> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);

        //调用service分页,会将查询到的数据自动存入page对象
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //名称模糊查询
        queryWrapper.like(StringUtils.isNotBlank(name), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(page, queryWrapper);

        /*
        // 获取分页对象中所有员工，挨个请求密码，并重新封装到一个list集合中
        List<Employee> employees = page.getRecords().stream().map((employee) -> {

            // 清空密码
            employee.setPassword(null);
            return employee;
        }).collect(Collectors.toList());

        // 清空过密码的员工集合，设置会分页对象
        page.setRecords(employees);*/

        // 获取所有员工
        List<Employee> employees = page.getRecords();
        for (int i = 0; i < employees.size(); i++) {
            employees.get(i).setPassword(null);
        }

        //组织数据并响应数据
        return R.success("查询成功", page);
    }


    /*修改员工状态*/
    @PutMapping
    public R update(@RequestBody Employee employee) {
        log.info("修改员工{}", employee);
        //请求参数健壮性判断
        if (employee.getId() != null) {
            //调用service更新用户
            boolean updateResult = employeeService.updateById(employee);

            if (updateResult) {
                return R.success("修改成功");
            }
            return R.fail("修改失败");
        }

        return R.fail("参数有误");
    }

    /*根据id编辑修改员工信息*/
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询，id为{}", id);
        if (id != null) {
            Employee employee = employeeService.getById(id);
            //清空密码
            employee.setPassword(null);
            return R.success("查询成功", employee);
        }
        return R.fail("参数有误!");
    }


}

