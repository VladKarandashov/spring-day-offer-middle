package com.onedayoffer.taskdistribution.services;

import com.onedayoffer.taskdistribution.DTO.EmployeeDTO;
import com.onedayoffer.taskdistribution.DTO.TaskDTO;
import com.onedayoffer.taskdistribution.DTO.TaskStatus;
import com.onedayoffer.taskdistribution.exception.BusinessException;
import com.onedayoffer.taskdistribution.exception.ExceptionStatus;
import com.onedayoffer.taskdistribution.repositories.EmployeeRepository;
import com.onedayoffer.taskdistribution.repositories.TaskRepository;
import com.onedayoffer.taskdistribution.repositories.entities.Employee;
import com.onedayoffer.taskdistribution.repositories.entities.Task;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public List<EmployeeDTO> getEmployees(@Nullable String sortDirection) {
        Sort sort = getSortByEmployeeFio(sortDirection);
        List<Employee> employeeList = employeeRepository.findAllAndSort(sort);
        log.info("Найдено {} записей Employee", employeeList.size());
        Type listType = new TypeToken<List<EmployeeDTO>>() {}.getType();
        return modelMapper.map(employeeList, listType);
    }

    @Transactional
    public EmployeeDTO getOneEmployee(Integer id) {
        Employee employee = getEmployeeById(id);
        log.info("Успешно найден employee с id = {}", id);
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    public List<TaskDTO> getTasksByEmployeeId(Integer id) {
        List<Task> taskListByEmployeeId = taskRepository.findAllByEmployeeId(id);
        log.info("Найдено {} записей Task", taskListByEmployeeId.size());
        Type listType = new TypeToken<List<TaskDTO>>() {}.getType();
        return modelMapper.map(taskListByEmployeeId, listType);
    }

    @Transactional
    public void changeTaskStatus(Integer employeeId, Integer taskId, TaskStatus status) {
        Task task = getTaskById(taskId);
        task.setStatus(status);
        taskRepository.save(task);
    }

    @Transactional
    public void postNewTask(Integer employeeId, TaskDTO newTask) {
        Task task = modelMapper.map(newTask, Task.class);
        Employee employee = getEmployeeById(employeeId);
        task.setEmployee(employee);
        taskRepository.save(task);
    }

    private Sort getSortByEmployeeFio(@Nullable String sortDirection) {
        if (StringUtils.isBlank(sortDirection)) {
            return Sort.unsorted();
        }
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDirection).orElseThrow(() -> {
            log.error("Не правильно задан порядок сортировки {} (ASС/DESC)", sortDirection);
            return new BusinessException(ExceptionStatus.VALIDATION_ERROR, "Порядок сортировки должен быть ASС/DESC");
        });
        return Sort.by(direction, "fio");
    }

    private Employee getEmployeeById(Integer employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> {
            log.error("Не найден employee с id = {}", employeeId);
            return new BusinessException(ExceptionStatus.NOT_FOUND_EMPLOYEE);
        });
    }

    private Task getTaskById(Integer taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> {
            log.error("Не найдена Task с id = {}", taskId);
            return new BusinessException(ExceptionStatus.NOT_FOUND_TASK);
        });
    }
}
