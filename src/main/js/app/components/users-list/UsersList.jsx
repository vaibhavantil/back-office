import React from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import { List } from 'semantic-ui-react';
import Pagination from '../pagination/Pagination';

const ListContainer = styled.div`
    width: 500px;
    border: solid 1px #dfe0e0;
    border-radius: 5px;
    padding: 20px 10px;
    margin-top: 30px;
`;

const ListItem = ({ item }) => (
    <Link to={`/messages/${item.hid}`} replace>
        <List.Content>
            {item.firstName ? (
                <List.Header>
                    {`${item.firstName} ${item.lastName || ''}`}
                </List.Header>
            ) : (
                <List.Header>User-{item.hid}</List.Header>
            )}
        </List.Content>
    </Link>
);

export default class UsersList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeList: []
        };
        this.onChangePage = this.onChangePage.bind(this);
    }

    onChangePage(activeList) {
        this.setState({ activeList });
    }

    render() {
        const { chats } = this.props;
        const { activeList } = this.state;
        return (
            <ListContainer>
                <List celled selection verticalAlign="middle" size="big">
                    {activeList.length ? (
                        activeList.map(item => (
                            <List.Item key={item.hid}>
                                <ListItem item={item} />
                            </List.Item>
                        ))
                    ) : (
                        <h2>Not found</h2>
                    )}
                </List>
                <Pagination items={chats} onChangePage={this.onChangePage} pageSize={4} />
            </ListContainer>
        );
    }
}
