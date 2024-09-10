// Componants/Category.js
import React, { useState, useEffect } from 'react';
import api, { fetchCategories } from './api';
import "../Css/Common.css";

function Category() {
    const [categories, setCategories] = useState([]);
    const [newCategory, setNewCategory] = useState('');
    const [editCategory, setEditCategory] = useState(null);
    const [editName, setEditName] = useState('');

    useEffect(() => {
        loadCategories();
    }, []);

    // 카테고리 목록 불러오기
    const loadCategories = async () => {
        const categories = await fetchCategories();
        setCategories(categories);
    };

    // 카테고리 추가
    const handleAddCategory = async () => {
        try {
            await api.post('/api/category/create', { name: newCategory });
            setNewCategory(''); // 입력 필드 초기화
            fetchCategories(); // 새로고침
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
            fetchCategories(); // 새로고침
        } catch (error) {
            console.error('카테고리 수정 중 오류 발생:', error);
        }
    };

    // 카테고리 삭제
    const handleDeleteCategory = async (id) => {
        try {
            await api.delete(`/api/category/delete/${id}`);
            fetchCategories(); // 새로고침
        } catch (error) {
            console.error('카테고리 삭제 중 오류 발생:', error);
        }
    };

    return (
        <div className='wrapper cateRegist'>
            <h1>카테고리 관리</h1>

            {/* 카테고리 추가 */}
            <div>
                <input
                    type="text"
                    placeholder="새 카테고리 이름"
                    value={newCategory}
                    onChange={(e) => setNewCategory(e.target.value)}
                />
                <button onClick={handleAddCategory}>추가</button>
            </div>

            {/* 카테고리 목록 */}
            <ul>
                {categories.length > 0 ? (
                    categories.map((category) => (
                        <li key={category.id}>
                            {editCategory === category.id ? (
                                <>
                                    <input
                                        type="text"
                                        value={editName}
                                        onChange={(e) => setEditName(e.target.value)}
                                    />
                                    <button onClick={() => handleEditCategory(category.id)}>저장</button>
                                    <button onClick={() => setEditCategory(null)}>취소</button>
                                </>
                            ) : (
                                <>
                                    {category.name}
                                    <button onClick={() => {
                                        setEditCategory(category.id);
                                        setEditName(category.name);
                                    }}>수정</button>
                                    <button onClick={() => handleDeleteCategory(category.id)}>삭제</button>
                                </>
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

export default Category;
