import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../api'; // api 모듈을 사용

const QuizDetail = () => {
    const { id } = useParams(); // URL에서 ID를 추출
    const [quiz, setQuiz] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedAnswer, setSelectedAnswer] = useState(''); // 선택된 답변 상태

    useEffect(() => {
        const fetchQuizDetails = async () => {
            try {
                const response = await api.get(`/api/quiz/${id}`); // 퀴즈 ID로 API 호출
                setQuiz(response.data);
            } catch (error) {
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchQuizDetails();
    }, [id]);

    const handleAnswerChange = (e) => {
        setSelectedAnswer(e.target.value); // 선택된 답변 상태 업데이트
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post('/api/quiz/submit', {
                categoryName: quiz.category.name,
                answer: selectedAnswer // 선택된 답변 전송
            });
            alert('Quiz submitted successfully');
        } catch (error) {
            console.error(error);
            alert('Failed to submit quiz');
        }
    };

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error.message}</p>;
    if (!quiz) return <p>No quiz found</p>;

    return (
        <div className="quiz-detail">
            <h1>{quiz.category.name} 퀴즈 풀이</h1> {/* 카테고리 이름을 제목에 추가 */}
            
            <p><strong>제목:</strong> {quiz.title}</p>
            {/* 퀴즈 옵션을 렌더링하는 부분 */}
            <form onSubmit={handleSubmit}>
                <input type="hidden" name="categoryName" value={quiz.category.name} />
                {quiz.options && Array.isArray(quiz.options) ? (
                    quiz.options.map((option, index) => (
                        <div key={index}>
                            <input
                                type="radio"
                                name="answer" // 모든 라디오 버튼이 동일한 name을 가져야 합니다
                                value={option}
                                checked={selectedAnswer === option} // 선택된 답변과 비교하여 체크 여부 설정
                                onChange={handleAnswerChange} // 답변 변경 시 호출되는 핸들러
                            />
                            <label>{option}</label>
                        </div>
                    ))
                ) : (
                    <p>No options available</p>
                )}
                <button type="submit" className="btn btn-primary">Submit Quiz</button>
            </form>
        </div>
    );
};

export default QuizDetail;
