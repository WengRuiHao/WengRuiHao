package chat.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chat.model.dto.Register;
import chat.model.dto.UserDto;
import chat.model.dto.Password;
import chat.model.dto.Profile;
import chat.model.entity.User;
import chat.repository.UserRepository;
import chat.service.UserService;
import chat.util.Hash;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Register addUser(Register register) {
		// 新增會員
		Optional <User> optUser= userRepository.findByUsername(register.getUsername());
		if(optUser.isPresent()) {
			throw new RuntimeException("此會員已存在，請重新註冊");
		}
		String salt = Hash.getSalt();
		String passwordHash = Hash.getHash(register.getPassword(), salt);
		User user = new User(register.getUsername(), passwordHash, salt, register.getGender(), register.getEmail());
		User addUser = userRepository.save(user);
		Register addRegister = modelMapper.map(addUser, Register.class);
		return addRegister;
	}

	@Override
	public List<Register> findAllUser() {
		// 查詢所有會員
		List<User> users = userRepository.findAll();
		List<Register> registers = users.stream().map(user -> modelMapper.map(user, Register.class))
				.collect(Collectors.toList());
		return registers;
	}

	@Override
	public Profile getUser(String username) {
		// 查詢個人資料
		System.out.println(username);
		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("此會員不存在"));
		Profile userDto = modelMapper.map(user, Profile.class);
		return userDto;
	}

	@Override
	public UserDto findByUser(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("此會員不存在"));
		UserDto userDto=modelMapper.map(user,UserDto.class);
		return userDto;
	}

	@Override
	public List<UserDto> finAllUserDtos() {
		// 查詢所有會員
		List<User> users = userRepository.findAll();
		List<UserDto>  userDtos= users.stream().map(user -> modelMapper.map(user, UserDto.class))
				.collect(Collectors.toList());
		return userDtos;
	}

	@Override
	public Profile updateProfile(Profile profile) {
		// 修改個人資料
		User user = userRepository.findByUsername(profile.getUsername())
				.orElseThrow(() -> new RuntimeException("此會員不存在"));
		user.setNickName(profile.getNickName());
		user.setGender(profile.getGender());
		user.setEmail(profile.getEmail());
		user.setProfileContent(profile.getProfileContent());
		userRepository.save(user);
		Profile updateProfile = modelMapper.map(user, Profile.class);
		return updateProfile;
	}

	@Override
	public Boolean updatePassword(String username,Password password) {
		User user= userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("此會員不存在，修改失敗"));
		String salt=user.getSalt();
		String OldpasswordHash=Hash.getHash(password.getOldpassword(), salt);
		if(!OldpasswordHash.equals(user.getPasswordHash())) {
			throw new RuntimeException("舊密碼輸入錯誤");
		}
		String NewpasswordHash=Hash.getHash(password.getNewpassword(), salt);
		user.setPasswordHash(NewpasswordHash);
		userRepository.save(user);
		return true;
	}

	@Override
	public Optional<Register> checkPassword(Register register) {
		// 登入
		Optional<User> optUser = userRepository.findByUsername(register.getUsername());
				if(optUser.isEmpty()) {
					throw new RuntimeException("此會員不存在");
				}
		String checkPasswordHash = Hash.getHash(register.getPassword(), optUser.get().getSalt());
		if (!checkPasswordHash.equals(optUser.get().getPasswordHash())) {
			throw new RuntimeException("密碼不正確");
		}
		return Optional.of(modelMapper.map(optUser.get(),Register.class));
	}

}
