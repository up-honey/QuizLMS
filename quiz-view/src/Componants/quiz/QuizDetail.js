import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom'; // useNavigate 추가

const QuizDetail = () => {
    const { quizId } = useParams(); // URL 파라미터에서 quizId 가져오기
    const navigate = useNavigate(); // useNavigate 훅 사용
    const [quiz, setQuiz] = useState(null);

    useEffect(() => {
        // 퀴즈 데이터를 API에서 가져오는 함수
        const fetchQuiz = async () => {
            try {
                const response = await fetch(`/api/quiz/detail/${quizId}`);
                const data = await response.json();
                setQuiz(data); // 퀴즈 데이터 설정
            } catch (error) {
                console.error("Error fetching quiz data:", error);
            }
        };

        fetchQuiz();
    }, [quizId]);

    const handleSubmit = async (event) => {
        event.preventDefault();
        const formData = new FormData(event.target);
    
        // 여러 개의 답변을 저장할 배열
        const answers = [];
        
        // 예시: formData에서 quizId와 answer를 반복적으로 가져오는 부분
        // 여기에 맞게 반복문을 사용하여 답변을 추가하세요.
        for (let i = 0; i < formData.getAll('quizId').length; i++) {
            answers.push({
                quizId: formData.getAll('quizId')[i],
                answer: formData.getAll('answer')[i],
            });
        }
    
        const data = {
            categoryName: formData.get('categoryName'),
            answers: answers,
        };
    
        try {
            // 데이터를 서버에 전송
            await fetch('/api/quiz/submit', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            });
    
            // 결과 페이지로 리다이렉트
            navigate('/result/list');
        } catch (error) {
            console.error("Error submitting quiz:", error);
        }
    };
    

    if (!quiz) return <div>Loading...</div>; // 로딩 중일 때

    return (
        <div>
            <h1>{quiz.category.name} Quiz</h1>
            <form onSubmit={handleSubmit}>
                <input type="hidden" name="categoryName" value={quiz.category.name} />
                <input type="hidden" name="quizId" value={quiz.id} />
                <h3>{quiz.title}</h3>
                <div>
                    {quiz.options.map((option, index) => (
                        <div key={index}>
                            <input type="radio" name="answer" value={option} />
                            <label>{option}</label>
                        </div>
                    ))}
                </div>
                <button type="submit">Submit Quiz</button>
            </form>
        </div>
    );
};

export default QuizDetail;
