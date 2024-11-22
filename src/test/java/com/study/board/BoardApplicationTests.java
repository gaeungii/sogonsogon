package com.study.board;

import com.study.board.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardApplicationTests {

	@Autowired
	private BoardService boardService;


}
