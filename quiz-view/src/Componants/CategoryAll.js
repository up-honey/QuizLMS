// Componants/Category.js
import React, { useEffect, useState } from 'react';
import "../Css/Common.css";
import api, { fetchCategories } from './api';

function CategoryAll() {
    const [categories, setCategories] = useState([]);
    const [newCategory, setNewCategory] = useState('');
    const [editCategory, setEditCategory] = useState(null);
    const [editName, setEditName] = useState('');

    //카테고리 리스트 호출
    useEffect(() => {
        const loadCategories = async () => {
            setCategories(await fetchCategories()); // await로 호출하여 결과를 바로 설정
        };
        loadCategories();
    }, []);

    // 카테고리 추가
    const handleAddCategory = async () => {
        try {
            await api.post('/api/category/create', { name: newCategory });
            setNewCategory(''); // 입력 필드 초기화
            // 새 카테고리 추가 후 categories를 다시 가져오기
            const updatedCategories = await fetchCategories(); // await로 결과를 받아옴
            setCategories(updatedCategories); // 상태 업데이트
        } catch (error) {
            console.error('카테고리 추가 중 오류 발생:', error);
        }
    };


    // 카테고리 수정
    const handleEditCategory = async (id) => {
        try {
            await api.put(`/api/category/modify/${id}`, { name: editName });
            setEditCategory(null);
            setEditName('');
            // 수정 후 categories를 다시 가져오기
            const updatedCategories = await fetchCategories();
            setCategories(updatedCategories); // 상태 업데이트
        } catch (error) {
            console.error('카테고리 수정 중 오류 발생:', error);
        }
    };


    // 카테고리 삭제
    const handleDeleteCategory = async (id) => {
        try {
            await api.delete(`/api/category/delete/${id}`);
            // 삭제 후 categories를 다시 가져오기
            const updatedCategories = await fetchCategories();
            setCategories(updatedCategories); // 상태 업데이트
        } catch (error) {
            console.error('카테고리 삭제 중 오류 발생:', error);
        }
    };


    return (
        <div className='wrapper cateRegist'>
            <h1>카테고리 관리</h1>
    
            {/* 카테고리 추가 */}
            <div className='add-category'>
                <input
                    type="text"
                    placeholder="새 카테고리 이름"
                    value={newCategory}
                    onChange={(e) => setNewCategory(e.target.value)}
                />
                <button onClick={handleAddCategory}>추가</button>
            </div>
    
            {/* 카테고리 목록 */}
            <ul className='category-list'>
                {categories.length > 0 ? (
                    categories.map((category) => (
                        <li key={category.id} className='category-item1'>
                            {editCategory === category.id ? (
                                <div className='edit-category'>
                                    <input
                                        type="text"
                                        value={editName}
                                        onChange={(e) => setEditName(e.target.value)}
                                    />
                                    <button onClick={() => handleEditCategory(category.id)}>저장</button>
                                    <button onClick={() => setEditCategory(null)}>취소</button>
                                </div>
                            ) : (
                                <div className='category-details'>
                                    {category.name}
                                    <button onClick={() => {
                                        setEditCategory(category.id);
                                        setEditName(category.name);
                                    }}>수정</button>
                                    <button onClick={() => handleDeleteCategory(category.id)}>삭제</button>
                                </div>
                            )}
                        </li>
                    ))
                ) : (
                    <li>카테고리가 없습니다.</li> // 카테고리가 없을 때 메시지 표시
                )}
            </ul>
        </div>
    );
    
}

export default CategoryAll;
