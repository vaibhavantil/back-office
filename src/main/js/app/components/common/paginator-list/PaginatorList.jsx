import React from 'react';
import { List } from 'semantic-ui-react';
import styled from 'styled-components';
import Pagination from '../pagination/Pagination';

const ListContainer = styled.div`
    width: 500px;
    border: solid 1px #dfe0e0;
    border-radius: 5px;
    padding: 20px 10px;
    margin-top: 30px;
`;

export default class PaginatorList extends React.Component {
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
        const { activeList } = this.state;
        const { list, itemContent } = this.props;
        return (
            <ListContainer>
                <List selection size="big">
                    {activeList.length ? (
                        activeList.map(item => (
                            <List.Item key={item.hid}>
                                {itemContent(item)}
                            </List.Item>
                        ))
                    ) : (
                        <h2>Not found</h2>
                    )}
                </List>
                <Pagination
                    items={list}
                    onChangePage={this.onChangePage}
                    pageSize={6}
                />
            </ListContainer>
        );
    }
}
