import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';

export default class AudioMessage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            url: ''
        };
    }

    componentDidMount() {
        const { content } = this.props;
        // todo remove if/else
        if (content.indexOf('{') + 1) {
            const data = JSON.parse(content);
            // todo move axios request to backend or /api dir
            axios
                .post('http://localhost:3056/convert', {
                    name: data.name,
                    content: data.content
                })
                .then(response => {
                    this.setState({ url: response.data.link });
                })
                .catch(error => {
                    //eslint-disable-next-line
                    console.log(error);
                });
        } else {
            this.setState({ url: content });
        }
    }

    render() {
        return <audio src={this.state.url} controls />;
    }
}

AudioMessage.propTypes = {
    content: PropTypes.string.isRequired
};
