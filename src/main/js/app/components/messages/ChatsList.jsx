import React from 'react';
import Chats from 'components/chats/Chats';
import { Input } from 'semantic-ui-react';


export default class ChatsList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoading: false,
            results: [],
            value: ''
        };
        this.handleSearchChange = this.handleSearchChange.bind(this);
        this.searchRequest = this.searchRequest.bind(this);
    }

    handleSearchChange(e) {
        const keyValue = e.key;
        this.setState({ value: e.target.value }, () => {
            if (keyValue === 'Enter') this.searchRequest();
        });
    }

    searchRequest() {
        this.setState({ isLoading: true });
        if (this.state.value) {
            this.props.search(this.props.client.token, this.state.value);
        }
    }

    componentWillReceiveProps(newProps) {
        if (newProps.chats.list.length) {
            this.setState({
                results: newProps.chats.list,
                isLoading: newProps.chats.requesting
            });
        }
    }

    render() {
        return (
            <React.Fragment>
                <Input
                    loading={this.state.isLoading}
                    placeholder="Search..."
                    iconPosition="left"
                    style={{ marginTop: '30px' }}
                    onKeyPress={this.handleSearchChange}
                    action={{ icon: 'search', onClick: this.searchRequest }}
                />
                <Chats chats={this.state.results} />
            </React.Fragment>
        );
    }
}
