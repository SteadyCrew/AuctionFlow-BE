package org.example.auctionflowbe.service;

import org.example.auctionflowbe.dto.StoreDTO;
import org.example.auctionflowbe.entity.Store;
import org.example.auctionflowbe.entity.User;
import org.example.auctionflowbe.repository.StoreRepository;
import org.example.auctionflowbe.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    private final UserRepository userRepository;
    public StoreDTO createStore(User user, StoreDTO storeDTO) {
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
    public StoreDTO updateStore(Long storeId, StoreDTO storeDTO) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("Store not found"));
        store.setName(storeDTO.getName());
        store.setContent(storeDTO.getContent());
        store.setPostcode(storeDTO.getPostcode());
        store.setBasicAddr(storeDTO.getBasicAddr());
        store.setDetailAddr(storeDTO.getDetailAddr());
        storeRepository.save(store);
        return storeDTO;
    }
    public StoreDTO getStore(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("Store not found"));
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setStoreId(store.getStoreId());
        storeDTO.setName(store.getName());
        storeDTO.setContent(store.getContent());
        storeDTO.setPostcode(store.getPostcode());
        storeDTO.setBasicAddr(store.getBasicAddr());
        storeDTO.setDetailAddr(store.getDetailAddr());
        storeDTO.setUserId(store.getUser().getUserId());
        return storeDTO;
    }
    public void deleteStore(Long storeId) {
        storeRepository.deleteById(storeId);
    }
}
