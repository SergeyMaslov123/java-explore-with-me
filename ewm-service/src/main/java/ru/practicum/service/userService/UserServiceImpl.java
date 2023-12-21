package ru.practicum.service.userService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.userDto.UserDto;
import ru.practicum.dto.userDto.UserMapper;
import ru.practicum.exception.ConflictEx;
import ru.practicum.exception.EntityNotFoundEx;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(List<Integer> ids, Integer from, Integer size) {
        if (from < 0 || size < 0) {
            throw new RuntimeException("некорректный запрос from, size");
        }
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        if (ids != null) {
            return userRepository.findAllByIdIn(ids, pageable).stream()
                    .map(UserMapper::toUserDtoFromUser)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll(pageable).stream()
                    .map(UserMapper::toUserDtoFromUser)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        if (userRepository.countByName(userDto.getName()) == 0) {
            return UserMapper.toUserDtoFromUser(userRepository.save(UserMapper.toUserFromUserDto(userDto)));
        } else {
            throw new ConflictEx("name is busy");
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundEx("Usr not found"));
        userRepository.deleteById(userId);
    }
}
