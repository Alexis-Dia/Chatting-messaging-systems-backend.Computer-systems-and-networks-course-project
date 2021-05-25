package alexeyd.com.controller;

import alexeyd.com.model.Message;
import alexeyd.com.repository.DefaultChatRepository;
import alexeyd.com.repository.NotTailableChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@AllArgsConstructor
public class ChatController {

	@Autowired
	private final DefaultChatRepository defaultChatRepository;

	@Autowired
	private final NotTailableChatRepository notTailableChatRepository;

	@GetMapping
	public String index() {
		return "index.html";
	}

	@PostMapping(path = "/channels/createNewChannel", consumes = APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Void>> createNewTopic(@RequestBody Message message) {
		message.setCreationDate(LocalDateTime.now());
		message.setTopic(message.getTopic());
		message.setId(System.currentTimeMillis());

		defaultChatRepository.save(message);
		return 	defaultChatRepository.save(message)
				.thenReturn(ResponseEntity.ok().<Void>build())
				.onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	}

	@PostMapping(path = "/channels/addNewMessage", consumes = APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Void>> store(@RequestBody Message message) {
		message.setCreationDate(LocalDateTime.now());
		message.setTopic(message.getTopic());
		message.setId(System.currentTimeMillis());

		return defaultChatRepository.save(message)
				.thenReturn(ResponseEntity.ok().<Void>build())
				.onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	}

	@GetMapping(value = "/channels/{topic}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Message> getAllByTopic(@PathVariable("topic") String topic) {
		return defaultChatRepository.findAllByTopic(topic);
	}

	@ResponseBody
	@GetMapping(value = "/channels/getAllChannels")
	public List<Message> getAllChannels() {
		List<Message> collect = defaultChatRepository.findAll().flatMapIterable(p -> Arrays.asList(new Message(p.getTopic(), p.getDeleted()))).distinct().toStream().collect(Collectors.toList());
		return collect;
	}

	@ResponseBody
	@PostMapping(value = "/channels/deleteChannel")
	public void deleteChannel(@RequestBody Message message) {
		List<Message> block = notTailableChatRepository.findAllByTopic(message.getTopic()).collectList().block();
		for (Message ob: block) {
			ob.setDeleted(true);
			notTailableChatRepository.save(ob).block();
		}

	}


}
