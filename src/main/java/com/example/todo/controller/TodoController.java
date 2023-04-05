package com.example.todo.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.TodoDTO;
import com.example.todo.model.TodoEntity;
import com.example.todo.service.TodoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("todo")
public class TodoController {
	
	@Autowired
	private TodoService service;
	
	@PostMapping
	public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
		try {
			log.info("Log : createTodo entrance");
			
			TodoEntity entity = TodoDTO.toEntity(dto);
			log.info("Log : dto => entity ok");
			
			entity.setUserId(userId);
			
			Optional<TodoEntity> entities = service.create(entity);
			log.info("Log : service.create ok");
			
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			log.info("Log : entities => dtos ok");
			
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			log.info("Log : responseDTO ok");
			
			return ResponseEntity.ok().body(response);
		}catch(Exception e) {
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
		
	}
	
	@GetMapping
	public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId){
		List<TodoEntity> entities = service.retrieve(userId);
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping("/update")
	public ResponseEntity<?> update(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
		try {
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			entity.setUserId(userId);
			
			Optional<TodoEntity> entities = service.update(entity);
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			return ResponseEntity.ok().body(response);
		}catch(Exception e) {
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@PutMapping
	public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
		try {
			TodoEntity entity = TodoDTO.toEntity(dto);
			entity.setUserId(userId);
			Optional<TodoEntity> entities = service.updateTodo(entity);
			
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			return ResponseEntity.ok().body(response);
		}catch(Exception e) {
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@DeleteMapping
	public ResponseEntity<?> delete(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
		try {
			TodoEntity entity = TodoDTO.toEntity(dto);
			entity.setUserId(userId);
			List<TodoEntity> entities = service.delete(entity);
			
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			return ResponseEntity.ok().body(response);
		}catch(Exception e) {
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
}
