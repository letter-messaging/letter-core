package com.github.ivanjermakov.lettercore.controller;

import com.github.ivanjermakov.lettercore.dto.PreviewDto;
import com.github.ivanjermakov.lettercore.dto.UserDto;
import com.github.ivanjermakov.lettercore.entity.User;
import com.github.ivanjermakov.lettercore.exception.InvalidEntityException;
import com.github.ivanjermakov.lettercore.exception.InvalidSearchFormatException;
import com.github.ivanjermakov.lettercore.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("search")
@Transactional
public class SearchControllerImpl implements SearchController {

	private final SearchService searchService;

	@Autowired
	public SearchControllerImpl(SearchService searchService) {
		this.searchService = searchService;
	}

	@Override
	@GetMapping("conversations")
	public List<PreviewDto> searchConversations(@ModelAttribute User user,
	                                            @RequestParam("search") String search,
	                                            Pageable pageable) throws InvalidEntityException {
		return searchService.searchConversations(user, search, pageable);
	}

	@Override
	@GetMapping("users")
	public List<UserDto> searchUsers(@ModelAttribute User user,
	                                 @RequestParam("search") String search,
	                                 Pageable pageable) throws InvalidSearchFormatException {
		return searchService.searchUsers(search, pageable);
	}

}
