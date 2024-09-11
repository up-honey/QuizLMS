// Components/QuizResult.js
import React from 'react';

function QuizResult({ correctCount, totalCount, userId, results, onSave, onTakeAnotherQuiz }) {
    return (
        <div>
            <h1>퀴즈 결과</h1>

            {/* 총 점수 표시 */}
            <div>
                <h2>총 점수: {correctCount} / {totalCount}</h2>
            </div>

            <form onSubmit={onSave}>
                <input type="hidden" name="userId" value={userId} />
                <input type="hidden" name="categoryName" value={results[0].quiz.category.name} />

                {results.map((result, index) => (
                    <div key={index}>
                        <p>
                            <strong>문제:</strong> {result.quiz.title}<br />
                            <strong>제출한 답:</strong>
                            <input type="text" name="userAnswers" defaultValue={result.answerGiven} required /><br />
                            <strong>정답:</strong> {result.quiz.answer}<br />
                            <strong>결과:</strong> {result.correct ? '정답' : '오답'}
                        </p>
                    </div>
                ))}

                <button type="submit">퀴즈 결과 저장</button>
            </form>

            <button onClick={onTakeAnotherQuiz}>다시 풀기</button>

            <form action="/result/list" method="get">
                <button type="submit" className="nav-link btn btn-link">퀴즈 결과 조회</button>
            </form>
        </div>
    );
}

export default QuizResult;
