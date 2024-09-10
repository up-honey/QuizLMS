// import React, { useState, useEffect } from "react";
// import "../Css/Common.css"; // CSS 파일 경로에 맞게 수정
// import api from "./api";

// function Category(){
//     const [categories, setCategories] = useState([]);
//     const [editCategory, setEditCategory] = useState(null);
//     const [editName, setEditName] = useState('');

//     useEffect(() => {
//         fetchCategories();
//     }, []);

//     const [formData, setFormData] = useState({
//         name: "",
//     });
    

//     // Handle input change
//     const handleChange = (e) => {
//         const { name, value } = e.target;
//         setFormData({
//             ...formData,
//             [name]: value,
//         });
//     };

//     // 카테고리 생성 함수
//     const handleCreateCategory = async (e) => {
//         e.preventDefault();

//         try {
//             const response = await fetch("/api/category/create", {
//                 method: "POST",
//                 headers: {
//                     "Content-Type": "application/json",
//                 },
//                 body: JSON.stringify(formData),
//             });
//             console.log(formData); // formData가 제대로 전달되는지 확인

//             console.log(response);
//             if (response.ok) {
//                 setFormData({name: ""}); // 입력 필드 초기화
//                 alert("카테고리 등록 성공");
//             } else {
//                 console.error("Failed to create category");
//             }
//         } catch (error) {
//             console.error("Error creating category:", error);
//         }
//     };

//     // 카테고리 목록 불러오기
//     const fetchCategories = async () => {
//         try {
//             const response = await api.get('/api/category/list');
//             setCategories(response.data.content);
//         } catch (error) {
//             console.error('카테고리 목록을 불러오는 중 오류 발생:', error);
//         }
//     };

//     // 카테고리 수정
//     const handleEditCategory = async (id) => {
//         try {
//             await api.put(`/api/category/modify/${id}`, { name: editName });
//             setEditCategory(null);
//             setEditName('');
//             fetchCategories(); // 새로고침
//         } catch (error) {
//             console.error('카테고리 수정 중 오류 발생:', error);
//         }
//     };

//     // 카테고리 삭제
//     const handleDeleteCategory = async (id) => {
//         try {
//             await api.delete(`/api/category/delete/${id}`);
//             fetchCategories(); // 새로고침
//         } catch (error) {
//             console.error('카테고리 삭제 중 오류 발생:', error);
//         }
//     };

//     return (
//         <div className="wrapper cateRegist">
//             <h1>카테고리 등록</h1>

//             <form onSubmit={handleCreateCategory}>
//                 <input
//                     type="text"
//                     name="name"
//                     value={formData.name}
//                     onChange={handleChange}
//                     placeholder="New Category"
//                     required
//                 />
//                 <button type="submit">등록</button>
//             </form>
//             {/* 카테고리 목록 */}
//             <ul>
//                 {categories.map((category) => (
//                     <li key={category.id}>
//                         {editCategory === category.id ? (
//                             <>
//                                 <input
//                                     type="text"
//                                     value={editName}
//                                     onChange={(e) => setEditName(e.target.value)}
//                                 />
//                                 <button onClick={() => handleEditCategory(category.id)}>저장</button>
//                                 <button onClick={() => setEditCategory(null)}>취소</button>
//                             </>
//                         ) : (
//                             <>
//                                 {category.name}
//                                 <button onClick={() => {
//                                     setEditCategory(category.id);
//                                     setEditName(category.name);
//                                 }}>수정</button>
//                                 <button onClick={() => handleDeleteCategory(category.id)}>삭제</button>
//                             </>
//                         )}
//                     </li>
//                 ))}
//             </ul>
//         </div>
//     );
// }

// export default Category;
