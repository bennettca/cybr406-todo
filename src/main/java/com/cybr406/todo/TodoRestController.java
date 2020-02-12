package com.cybr406.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class TodoRestController {
    @Autowired
    InMemoryTodoRepository inMemoryTodoRepository;

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> getID(@PathVariable long id) {
        Optional<Todo> listID = inMemoryTodoRepository.find(id);
        if(listID.isPresent())
        {
            Todo newID = listID.get();
            return new ResponseEntity<>(newID, HttpStatus.OK);
        }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/todos/{id}/tasks")
    public ResponseEntity<Todo> addATask(@PathVariable long id, @RequestBody Task task)
    {
        Todo todo = inMemoryTodoRepository.addTask(id, task);
        List<Task> taskList = todo.getTasks();

        if(taskList.isEmpty() == false)
        {
            return new ResponseEntity<>(todo, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(todo, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/todos")
    public ResponseEntity<Todo> register(@Valid @RequestBody Todo todo) {

        if(todo.getAuthor().isEmpty() || todo.getDetails().isEmpty())
        {
            return new ResponseEntity<>(inMemoryTodoRepository.create(todo), HttpStatus.BAD_REQUEST);
        }
            return new ResponseEntity<>(inMemoryTodoRepository.create(todo), HttpStatus.CREATED);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<Todo> listOfTodos = inMemoryTodoRepository.findAll(page, size);
        return new ResponseEntity<>(listOfTodos, HttpStatus.OK);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<Todo> deleteTodo(@PathVariable long id) {

        try {
            inMemoryTodoRepository.delete(id);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Todo> deleteTask(@PathVariable long id) {
        try
        {
            inMemoryTodoRepository.deleteTask(id);
        }
        catch (NoSuchElementException E )
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
