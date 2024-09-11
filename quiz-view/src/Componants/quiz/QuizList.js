import React, { useState, useEffect } from 'react';
import api from '../api'; // axios 대신 api 사용
import { Link, useLocation } from 'react-router-dom';

const QuizList = () => {
    const [quizzes, setQuizzes] = useState([]);
    const [paging, setPaging] = useState({ totalPages: 1, number: 0, hasNext: false, hasPrevious: false });
    const location = useLocation();
    
    // 페이지 번호 파라미터를 얻기 위한 방법
    const query = new URLSearchParams(location.search);
    const currentPage = query.get('page') ? parseInt(query.get('page'), 10) : 0;

    useEffect(() => {
        // API 경로를 /api/quiz/list로 변경
        api.get(`/api/quiz/list?page=${currentPage}`)
            .then(response => {
                // 응답 데이터의 구조를 맞춤
                const { content, totalPages, number, hasNext, hasPrevious } = response.data;
                setQuizzes(content || []); // content가 undefined일 경우 빈 배열
                setPaging({ totalPages, number, hasNext, hasPrevious }); // 페이징 상태 업데이트
            })
            .catch(error => console.error('Error fetching quizzes:', error));
    }, [currentPage]);

    const handleDelete = (id) => {
        if (window.confirm('정말 삭제하시겠습니까?')) {
            api.delete(`/api/quiz/delete/${id}`)
                .then(() => {
                    // 삭제 후 퀴즈 목록을 다시 불러옴
                    setQuizzes(quizzes.filter(quiz => quiz.id !== id));
                })
                .catch(error => console.error('Error deleting quiz:', error));
        }
    };
    

    return (
        <div className=" quiz">
            <div className="wrapper">
                <h1>퀴즈 목록</h1>
                <Link to="/quiz/create" className="btn btn-primary">퀴즈 등록</Link>
                <table className="table table-bordered mt-3">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>카테고리</th>
                            <th>제목</th>
                            <th>정답</th>
                            <th>액션</th>
                        </tr>
                    </thead>
                    <tbody>
                        {quizzes.length > 0 ? (
                            quizzes.map(quiz => (
                                <tr key={quiz.id}>
                                    <td>{quiz.id}</td>
                                    <td>{quiz.category.name}</td>
                                    <td>{quiz.title}</td>
                                    <td>{quiz.answer}</td>
                                    <td>
                                        <Link to={`/quiz/modify/${quiz.id}`} className="btn btn-warning">수정</Link>
                                        <button 
                                            className="btn btn-danger"
                                            onClick={() => handleDelete(quiz.id)}
                                        >
                                            삭제
                                        </button>
                                        {/* <Link to={`/quiz/delete/${quiz.id}`} className="btn btn-danger"
                                            onClick={() => window.confirm('정말 삭제하시겠습니까?')}>삭제</Link> */}
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="5">퀴즈가 없습니다.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
                <div>
                    <nav>
                        <ul className="pagination">
                            <li className={`page-item ${paging.hasPrevious ? '' : 'disabled'}`}>
                                <Link className="page-link" to={`/quiz/list?page=${paging.number - 1}`}>이전</Link>
                            </li>
                            {Array.from({ length: paging.totalPages }).map((_, i) => (
                                <li key={i} className={`page-item ${i === paging.number ? 'active' : ''}`}>
                                    <Link className="page-link" to={`/quiz/list?page=${i}`}>{i + 1}</Link>
                                </li>
                            ))}
                            <li className={`page-item ${paging.hasNext ? '' : 'disabled'}`}>
                                <Link className="page-link" to={`/quiz/list?page=${paging.number + 1}`}>다음</Link>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>
    );
};

export default QuizList;
