package org.example.auctionflowbe.service;

import org.example.auctionflowbe.dto.StoreDTO;
import org.example.auctionflowbe.entity.Store;
import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.exception.ExceptionMessage;
import org.example.auctionflowbe.repository.StoreRepository;
import org.example.auctionflowbe.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyStoreService {

    private final StoreRepository storeRepository;

    private final UserRepository userRepository;
    public StoreDTO createStore(User user, StoreDTO storeDTO) {

        if (user == null) { // 사용자 확인
            throw new ExceptionMessage("사용자를 찾을 수 없습니다.");
        }
        // 상점이 이미 존재하는지 확인
        Store existingStore = storeRepository.findByUser(user);
        if(existingStore != null){
            throw new ExceptionMessage("해당 사용자의 상점이 이미 존재합니다.");
        }

        Store store = new Store();
        store.setName(storeDTO.getName());
        store.setContent(storeDTO.getContent());
        store.setPostcode(storeDTO.getPostcode());
        store.setBasicAddr(storeDTO.getBasicAddr());
        store.setDetailAddr(storeDTO.getDetailAddr());
        store.setUser(user);
        store = storeRepository.save(store);
        storeDTO.setStoreId(store.getStoreId());
        storeDTO.setUserId(user.getUserId());
        return storeDTO;
    }

    // 사용자 상점 정보 조회 => 사용자 당 하나의 상점 존재
    public StoreDTO getUserStoreInfo(User user){
        Store store = storeRepository.findByUser(user);
        if(store == null)
            throw new RuntimeException("사용자의 상점이 존재하지 않습니다.");
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setStoreId(store.getStoreId());
        storeDTO.setName(store.getName());
        storeDTO.setContent(store.getContent());
        storeDTO.setPostcode(store.getPostcode());
        storeDTO.setBasicAddr(store.getBasicAddr());
        storeDTO.setDetailAddr(store.getDetailAddr());
        storeDTO.setUserId(user.getUserId());

        return storeDTO;
    }

    public StoreDTO updateStore(User user, StoreDTO storeDTO) {
        Store store = storeRepository.findByUser(user);
        if (store == null) {
            throw new RuntimeException("사용자의 상점이 존재하지 않습니다.");
        }
        store.setName(storeDTO.getName());
        store.setContent(storeDTO.getContent());
        store.setPostcode(storeDTO.getPostcode());
        store.setBasicAddr(storeDTO.getBasicAddr());
        store.setDetailAddr(storeDTO.getDetailAddr());
        storeRepository.save(store);

        // 변경 내용 저장
        storeDTO.setStoreId(store.getStoreId());
        storeDTO.setUserId(store.getUser().getUserId());

        return storeDTO;
    }


    public void deleteStore(User user) {
        Store store = storeRepository.findByUser(user);
        if (store == null) {
            throw new RuntimeException("사용자의 상점이 존재하지 않습니다.");
        }
        storeRepository.delete(store);
    }
}
