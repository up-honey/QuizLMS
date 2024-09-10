import React, { useState } from 'react';
import api from '../api'; // api 모듈을 사용
import { useNavigate } from 'react-router-dom';

const QuizCreate = () => {
    const navigate = useNavigate();
    const [form, setForm] = useState({
        title: '',
        categoryName: '',
        answer: '',
        options: ['', '', '', ''] // 옵션을 4개로 초기화
    });

    const handleChange = (e) => {
        const { name, value, dataset } = e.target;
        if (name === 'options') {
            const index = parseInt(dataset.index, 10);
            setForm(prevForm => {
                const newOptions = [...prevForm.options];
                newOptions[index] = value;
                return { ...prevForm, options: newOptions };
            });
        } else {
            setForm(prevForm => ({ ...prevForm, [name]: value }));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post('/api/quiz/create', form);
            alert('Quiz created successfully');
            navigate('/quiz/list');
        } catch (error) {
            console.error(error);
            alert('Failed to create quiz');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>Title:</label>
                <input
                    type="text"
                    name="title"
                    value={form.title}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label>Category Name:</label>
                <input
                    type="text"
                    name="categoryName"
                    value={form.categoryName}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label>Answer:</label>
                <input
                    type="text"
                    name="answer"
                    value={form.answer}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label>Options:</label>
                {form.options.map((option, index) => (
                    <div key={index}>
                        <input
                            type="text"
                            name="options"
                            data-index={index}
                            value={option}
                            onChange={handleChange}
                            required
                        />
                    </div>
                ))}
            </div>
            <button type="submit">Create Quiz</button>
        </form>
    );
};

export default QuizCreate;
