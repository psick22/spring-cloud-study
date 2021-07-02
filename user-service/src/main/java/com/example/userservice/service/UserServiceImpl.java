package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.repository.UserEntity;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Environment env;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final ModelMapper mapper;
    //    private final RestTemplate restTemplate;
    private final OrderServiceClient orderServiceClient;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(encoder.encode(userDto.getPwd()));
        userRepository.save(userEntity);

        UserDto createdUser = mapper.map(userEntity, UserDto.class);

        return createdUser;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDto userDto = mapper.map(userEntity, UserDto.class);

        /**
         * REST Template 방식
         */
//        String orderUrl = String.format(env.getProperty("order_service.url"), userId);
//        ResponseEntity<List<ResponseOrder>> ordersResponse
//            = restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//            new ParameterizedTypeReference<List<ResponseOrder>>() {
//            });
//        List<ResponseOrder> orders = ordersResponse.getBody();
//        userDto.setOrders(orders);

        /**
         * Feign Client 방식
         */
        List<ResponseOrder> orders = orderServiceClient.getOrders(userId);
        userDto.setOrders(orders);

        return userDto;

    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity findUser = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(email));

        return mapper.map(findUser, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity findUser = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));

        return new User(
            findUser.getEmail(),
            findUser.getEncryptedPwd(),
            true,
            true,
            true,
            true,
            new ArrayList<>()
        );

    }
}
