import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';




const MyResult = () => {
    const [totalQuizCount, setTotalQuizCount] = useState(0);
    const [correctQuizCount, setCorrectQuizCount] = useState(0);
    const [paging, setPaging] = useState({ content: [], totalPages: 0, number: 0, hasPrevious: false, hasNext: false });
    const navigate = useNavigate();

    const fetchQuizResults = async (page = 0) => {
        try {
            const response = await fetch(`/api/result/list?page=${page}`);
            const data = await response.json();
            setTotalQuizCount(data.totalQuizCount);
            setCorrectQuizCount(data.correctQuizCount);
            setPaging({ content: data.results, totalPages: Math.ceil(data.totalQuizCount / 10), number: page, hasPrevious: page > 0, hasNext: page < Math.ceil(data.totalQuizCount / 10) - 1 });
        } catch (error) {
            console.error('퀴즈 결과를 가져오는 데 오류가 발생했습니다:', error);
        }
    };

    useEffect(() => {
        fetchQuizResults(); // 초기 페이지 로드
    }, []);

    // 데이터가 로드되기 전에는 로딩 메시지를 표시합니다.
    if (!paging || !paging.content) {
        return <div>로딩 중...</div>;
    }

    return (
        <div>
            {/* 전체 퀴즈와 맞춘 퀴즈 수 표시 */}
            <div className="alert alert-info">
                <strong>총 퀴즈 수:</strong> <span>{totalQuizCount}</span> |
                <strong> 맞춘 퀴즈 수:</strong> <span>{correctQuizCount}</span>
            </div>

            <table className="table table-bordered">
                <thead>
                    <tr>
                        <th>퀴즈 제목</th>
                        <th>카테고리</th>
                        <th>정답 여부</th>
                        <th>제출한 답변</th>
                        <th>행동</th>
                    </tr>
                </thead>
                <tbody>
                            {paging.content.map((results) => (
                    <tr key={results.quiz.id}>
                        <td>{results.quiz.title}</td>
                        <td>{results.quiz.category.name}</td>
                        <td>{results.correct ? '정답' : '오답'}</td>
                        <td>{results.answerGiven}</td>
                        <td>
                            {!results.correct ? (
                                <button onClick={() => navigate(`/quiz/detail/${results.quiz.id}`)} className="btn btn-warning">다시 풀기</button>
                            ) : (
                                <button onClick={() => navigate(`/quiz/solution/${results.quiz.id}`)} className="btn btn-info">상세 보기</button>
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            {/* 페이지네이션 */}
            <nav>
                <ul className="pagination">
                    <li className={`page-item ${paging.hasPrevious ? '' : 'disabled'}`}>
                        <button className="page-link" onClick={() => fetchQuizResults(paging.number - 1)} disabled={!paging.hasPrevious}>이전</button>
                    </li>
                    {[...Array(paging.totalPages)].map((_, i) => (
                        <li key={i} className={`page-item ${i === paging.number ? 'active' : ''}`}>
                            <button className="page-link" onClick={() => fetchQuizResults(i)}>{i + 1}</button>
                        </li>
                    ))}
                    <li className={`page-item ${paging.hasNext ? '' : 'disabled'}`}>
                        <button className="page-link" onClick={() => fetchQuizResults(paging.number + 1)} disabled={!paging.hasNext}>다음</button>
                    </li>
                </ul>
            </nav>

            <button onClick={() => navigate('/')} className="btn btn-primary">메인 화면으로 돌아가기</button>
        </div>
    );
};

export default MyResult;
