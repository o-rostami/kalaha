package com.bol.service.game;


import com.bol.exception.BusinessException;
import com.bol.exception.NotFoundException;
import com.bol.model.entity.GameEntity;
import com.bol.model.entity.PlayerEntity;
import com.bol.model.enums.DifficultyLevel;

/**
 * A <i>GameService</i>. This interface has responsibility to create and
 * control the game and movements.<p>
 * The <tt>GameService</tt> interface provides five methods for controlling
 * the game Service implemented by <tt>GameServiceImp</tt> class.<p>
 *
 * @author Omid Rostami
 */
public interface GameService {

    /**
     * Returns the created game based on the player and specified difficulty level.
     *
     * @param player          the Player of the game who created it for the first time
     * @param difficultyLevel the difficulty Level includes EASY, MEDIUM, HARD
     * @return the game entity just created.
     */

    GameEntity createGame(PlayerEntity player, DifficultyLevel difficultyLevel);

    /**
     * Connect second player to the game base on game id
     *
     * @param player the player who wants to connect the game
     * @param gameId the specified game id to connect
     * @return the game entity with connected person
     * @throws BusinessException if the second player is already connected or the two players are the same
     */

    GameEntity connect(PlayerEntity player, Long gameId);

    /**
     * Connect second player to a random game
     *
     * @param player the player who wants to connect to a random game
     * @return the game entity with connected person
     * @throws BusinessException if two players are the same or can not find a game with empty second player
     */

    GameEntity connectToRandomGame(PlayerEntity player);

    /**
     * Return a random game detail based on game id
     *
     * @param gameId id of game which want to get detail
     * @return the game entity
     * @throws NotFoundException if game does not exist
     */

    GameEntity getGameById(Long gameId);

    /**
     * Sow every stone based on game rules and show the winner
     *
     * @param gameId id of game which is currently being played
     * @param pitId  id of pit clicked
     * @return the game entity with details
     * @throws BusinessException if pit number chosen wrongly or game has already been finished
     */

    GameEntity gamePlay(Long gameId, Integer pitId);

}
