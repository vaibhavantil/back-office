import React from 'react';
import { Button } from 'semantic-ui-react';
import { getPageState } from 'app/lib/paginator';

/* eslint-disable react/prop-types */
export default class Pagination extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            pageState: {}
        };
        this.setPage = this.setPage.bind(this);
    }

    componentWillMount() {
        const { items, initialPage } = this.props;
        if (items && items.length) this.setPage(initialPage);
    }

    componentDidUpdate(prevProps) {
        if (this.props.items !== prevProps.items) {
            this.setPage(this.props.initialPage);
        }
    }

    setPage(page) {
        const { items, onChangePage } = this.props;

        const newPageState = getPageState(items.length, page);
        const pageOfItems = items.slice(
            newPageState.startIndex,
            newPageState.endIndex + 1
        );

        if (pageOfItems.length === 0) {
            this.setState({ pageState: newPageState });
            onChangePage(pageOfItems);
            return;
        }
        if (page < 1 || page > newPageState.totalPages) return;

        this.setState({ pageState: newPageState });
        onChangePage(pageOfItems);
    }

    render() {
        const { pageState } = this.state;

        return (
            <div className="pagination">
                {pageState.pages &&
                    pageState.pages.length > 1 && (
                        <Button.Group>
                            <Button
                                disabled={pageState.currentPage === 1}
                                onClick={this.setPage.bind(this, 1)}
                            >
                                First
                            </Button>
                            {pageState.pages.map((page, id) => (
                                <Button
                                    key={id}
                                    className={
                                        pageState.currentPage === page
                                            ? 'active'
                                            : ''
                                    }
                                    onClick={this.setPage.bind(this, page)}
                                >
                                    {page}
                                </Button>
                            ))}
                            <Button
                                disabled={
                                    pageState.currentPage ===
                                    pageState.totalPages
                                }
                                onClick={this.setPage.bind(
                                    this,
                                    pageState.totalPages
                                )}
                            >
                                Last
                            </Button>
                        </Button.Group>
                    )}
            </div>
        );
    }
}

Pagination.defaultProps = {
    initialPage: 1
};
