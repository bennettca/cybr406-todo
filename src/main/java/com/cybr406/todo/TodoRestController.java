package com.cybr406.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class TodoRestController
	{
	@Autowired
	InMemoryTodoRepository inMemoryTodoRepository;
	
	@Autowired
	TaskJpaRepository taskJpaRepository;
	
	@Autowired
	TodoRepository todoRepository;
	
	@Autowired
	TodoJpaRepository todoJpaRepository;
	
	@GetMapping("/todos/{id}")
	public ResponseEntity<Todo> getID(@PathVariable long id)
		{
			Optional<Todo> listID = todoJpaRepository.findById(id);
			
			if(listID.isPresent())
				{
					Todo newID = listID.get();
					return new ResponseEntity<>(newID , HttpStatus.OK);
				}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	
	@PostMapping("/todos/{id}/tasks")
	public ResponseEntity<Todo> addATask(@PathVariable long id , @RequestBody Task task)
		{
			return todoJpaRepository.findById(id)
					.map(todo ->
						 {
							 todo.getTasks().add(task);
							 task.setTodo(todo);
							 taskJpaRepository.save(task);
							 return new ResponseEntity<>(todo , HttpStatus.CREATED);
						 }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
		}
	
	@PostMapping("/todos")
	public ResponseEntity<Todo> createToDo(@Valid @RequestBody Todo todo)
		{
			
			Todo creation = todoJpaRepository.save(todo);
			return new ResponseEntity<>(creation , HttpStatus.CREATED);
		}
	
	@GetMapping("/todos")
	public Page<Todo> findAll(Pageable page)
		
		{
			return todoJpaRepository.findAll(page);
		}
	
	@DeleteMapping("/todos/{id}")
	public ResponseEntity<Todo> deleteTodo(@PathVariable long id)
		{
			
			try
				{
					todoJpaRepository.deleteById(id);
				}
			catch (NoSuchElementException e)
				{
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	
	@DeleteMapping("/tasks/{id}")
	public ResponseEntity<Todo> deleteTask(@PathVariable long id)
		{
			try
				{
					taskJpaRepository.deleteById(id);
				}
			catch (NoSuchElementException E)
				{
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
