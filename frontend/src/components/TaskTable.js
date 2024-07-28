import React, { useState } from 'react';
import axios from 'axios';

const TaskTable = ({ tasks, fetchUsers, selectedUserId }) => {
    const [editTaskId, setEditTaskId] = useState(null);
    const [editedTask, setEditedTask] = useState({ title: '', description: '' });

    const deleteTask = async (taskId) => {
        try {
            await axios.delete(`http://localhost:8081/api/tasks/${taskId}`);
            fetchUsers(); 
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
                userId: selectedUserId
            };
            await axios.put(`http://localhost:8081/api/tasks/${taskId}`, taskPayload);
            setEditTaskId(null);
            fetchUsers(); 
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

    const handleCancelClick = () => {
        setEditTaskId(null);
    }

    return (
        <div className='container-fluid'>
            <h2 className="my-4">Tasks</h2>
            <table className="table table-striped table-hover">
                <thead className="thead-dark">
                    <tr>
                        <th>Id</th>
                        <th>Title</th>
                        <th>Description</th>
                        <th>Deleted</th>
                        <th style={{ width: '200px' }}>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {tasks.map(task => (
                        <tr key={task.id}>
                            {editTaskId === task.id ? (
                                <>
                                    <td>{task.id}</td>
                                    <td><input type="text" name="title" className="form-control" value={editedTask.title} onChange={handleChange} /></td>
                                    <td><input type="text" name="description" className="form-control" value={editedTask.description} onChange={handleChange} /></td>
                                    <td>{task.deleted.toString()}</td>
                                    <td>
                                        <button className="btn btn-success w-100" onClick={() => handleSaveClick(task.id)}>Save</button>
                                        <button className="btn btn-secondary w-100" onClick={handleCancelClick}>Cancel</button>
                                    </td>
                                </>
                            ) : (
                                <>
                                    <td>{task.id}</td>
                                    <td>{task.title}</td>
                                    <td>{task.description}</td>
                                    <td>{task.deleted.toString()}</td>
                                    <td>
                                        <button className="btn btn-secondary w-100" onClick={() => handleEditClick(task)}>Edit</button>
                                        <button className="btn btn-danger w-100" onClick={() => deleteTask(task.id)} disabled={task.deleted}>Delete</button>
                                    </td>
                                </>
                            )}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default TaskTable;
