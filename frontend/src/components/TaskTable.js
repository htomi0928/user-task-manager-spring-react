import React, { useState } from 'react';
import axios from 'axios';

const TaskTable = ({ tasks, fetchUsers }) => {
    const [editTaskId, setEditTaskId] = useState(null);
    const [editedTask, setEditedTask] = useState({ title: '', description: '' });

    const deleteTask = async (taskId) => {
        try {
            await axios.delete(`http://localhost:8081/api/tasks/${taskId}`);
            fetchUsers(); // Refresh the user list after task deletion
        } catch (error) {
            console.error('Error deleting task', error);
        }
    };

    const handleEditClick = (task) => {
        setEditTaskId(task.id);
        setEditedTask({ title: task.title, description: task.description });
    };

    const handleSaveClick = async (taskId) => {
        try {
            const taskPayload = {
                title: editedTask.title,
                description: editedTask.description,
                userId: tasks.find(task => task.id === taskId).user.id // Assuming tasks have a user field
            };
            await axios.put(`http://localhost:8081/api/tasks/${taskId}`, taskPayload);
            setEditTaskId(null);
            fetchUsers(); // Refresh the user list after task update
        } catch (error) {
            console.error('Error updating task', error);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEditedTask(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    return (
        <div>
            <h2>Tasks</h2>
            <table className="table">
                <thead>
                    <tr>
                        <th>Id</th>
                        <th>Title</th>
                        <th>Description</th>
                        <th>Deleted</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {tasks.map(task => (
                        <tr key={task.id}>
                            <td>{task.id}</td>
                            <td>
                                {editTaskId === task.id ? (
                                    <input
                                        type="text"
                                        name="title"
                                        value={editedTask.title}
                                        onChange={handleChange}
                                    />
                                ) : (
                                    task.title
                                )}
                            </td>
                            <td>
                                {editTaskId === task.id ? (
                                    <input
                                        type="text"
                                        name="description"
                                        value={editedTask.description}
                                        onChange={handleChange}
                                    />
                                ) : (
                                    task.description
                                )}
                            </td>
                            <td>{task.deleted.toString()}</td>
                            <td>
                                {editTaskId === task.id ? (
                                    <button className="btn btn-primary" onClick={() => handleSaveClick(task.id)}>Save</button>
                                ) : (
                                    <button className="btn btn-secondary" onClick={() => handleEditClick(task)}>Edit</button>
                                )}
                                <button className="btn btn-danger" onClick={() => deleteTask(task.id)} disabled={task.deleted}>Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default TaskTable;
