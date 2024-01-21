package iut.chronoclash.chronoclash_api.api.service;

import iut.chronoclash.chronoclash_api.api.dto.GameDTO;
import iut.chronoclash.chronoclash_api.api.model.Game;
import iut.chronoclash.chronoclash_api.api.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class GameService {
    @Autowired
    GameRepository gameRepository;
    @Autowired
    UserService userService;

    public Game createGame(GameDTO dto) {
        Game game = new Game();
        game.setOwner(userService.findById(dto.getOwner()));
        game.setEnemy(dto.getEnemy());
        game.setWin(dto.isWin() ? 1 : 0);
        game.setDate(new Date());
        return gameRepository.save(game);
    }
}
